/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import controllers.actions.{DataRequiredAction, DataRetrievalAction, FakeDataRetrievalAction, FakeIdentifierAction}
import forms.ContactPreferencesFormProvider
import models.{ContactPreferences, NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import pages.ContactPreferencesPage
import play.api.http.Status.{BAD_REQUEST, OK, SEE_OTHER}
import play.api.i18n.MessagesApi
import play.api.mvc.{Call, ControllerComponents}
import play.api.test.Helpers.baseApplicationBuilder.injector
import play.api.test.Helpers.{GET, POST, defaultAwaitTimeout, redirectLocation, status}
import play.api.test.{FakeRequest, Helpers}
import repositories.SessionRepository
import views.html.ContactPreferencesView

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ContactPreferencesControllerSpec2 extends PlaySpec with BeforeAndAfterEach with MockitoSugar{
  val cc: ControllerComponents = Helpers.stubControllerComponents()
  private val userAnswersId: String = "id"

  private def emptyUserAnswers : UserAnswers = UserAnswers(userAnswersId)
  private val userAnswers = Some(emptyUserAnswers)
  private val nonEmptyUserAnswers = Some(UserAnswers(userAnswersId).set(ContactPreferencesPage, ContactPreferences.values.toSet).success.value)
  lazy val contactPreferencesRoute = routes.ContactPreferencesController.onPageLoad(NormalMode).url

  private val mockSessionRepository = mock[SessionRepository]
  private val mockNavigator = mock[Navigator]
  private val fakeIdentifierAction = new FakeIdentifierAction(cc.parsers)
  private val fakeDataRetrievalAction = new FakeDataRetrievalAction(userAnswers)
  private val mockDataRequiredAction = mock[DataRequiredAction]
  private val mockContactPreferencesFormProvider = new ContactPreferencesFormProvider()

  private val view = injector.instanceOf[ContactPreferencesView]
  private val formProvider = new ContactPreferencesFormProvider()
  private val form = formProvider()

  def onwardRoute = Call("GET", "/foo")

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockSessionRepository, mockNavigator)
  }

  private def createController(navigator: Navigator,dataRetrievalAction: DataRetrievalAction) = {
    new ContactPreferencesController(
      Helpers.stubMessagesApi(),
      mockSessionRepository,
      navigator,
      fakeIdentifierAction,
      dataRetrievalAction,
      mockDataRequiredAction,
      mockContactPreferencesFormProvider,
      Helpers.stubMessagesControllerComponents(), view
    )
  }

  val stubbedControllerDefault = createController(mockNavigator,fakeDataRetrievalAction)

  "ContactPreferences Controller" must {
    "new test must return ok and the correct view for a GET in " in{
      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)
      val view = injector.instanceOf[ContactPreferencesView]
      val fakeRequest = FakeRequest(GET, contactPreferencesRoute)
      val messages = injector.instanceOf[MessagesApi].preferred(fakeRequest)

      val result = stubbedControllerDefault.onPageLoad(NormalMode).apply(fakeRequest)

      status(result) mustBe OK
      //TODO check how to populate configuration correctly? also do we need to test views
     // contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages).toString
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {
      val view = injector.instanceOf[ContactPreferencesView]
      val fakeRequest = FakeRequest(GET, contactPreferencesRoute)
      val messages = injector.instanceOf[MessagesApi].preferred(fakeRequest)
      val testSpecificStubbedController = createController(mockNavigator,new FakeDataRetrievalAction(nonEmptyUserAnswers))

      val result = testSpecificStubbedController.onPageLoad(NormalMode).apply(fakeRequest)
      status(result) mustEqual OK
       // contentAsString(result) mustEqual view(form.fill(ContactPreferences.values.toSet), NormalMode)(fakeRequest, messages).toString
    }

    "must redirect to the next page when valid data is submitted" in {
      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val fakePostRequest = FakeRequest(POST, contactPreferencesRoute).withFormUrlEncodedBody(("value[0]", ContactPreferences.values.head.toString))
      val testSpecificStubbedController = createController(new FakeNavigator(onwardRoute),new FakeDataRetrievalAction(nonEmptyUserAnswers))

      val result = testSpecificStubbedController.onSubmit(NormalMode).apply(fakePostRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual onwardRoute.url
    }

    "must return a Bad Request and errors when invalid data is submitted" in {
      val fakePostRequest = FakeRequest(POST, contactPreferencesRoute).withFormUrlEncodedBody(("value", "invalid value"))
      val messages = injector.instanceOf[MessagesApi].preferred(fakePostRequest)
      val view = injector.instanceOf[ContactPreferencesView]
      val testSpecificStubbedController = createController(new FakeNavigator(onwardRoute),new FakeDataRetrievalAction(userAnswers))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = testSpecificStubbedController.onSubmit(NormalMode).apply(fakePostRequest)

      status(result) mustEqual BAD_REQUEST
     // contentAsString(result) mustEqual view(boundForm, NormalMode)(fakePostRequest, messages).toString
    }

    "must correctly load the page for a GET if no existing data is found" in {
      val fakeRequest = FakeRequest(GET, contactPreferencesRoute)
      val messages = injector.instanceOf[MessagesApi].preferred(fakeRequest)
      val view = injector.instanceOf[ContactPreferencesView]
      val testSpecificStubbedController = createController(mockNavigator,new FakeDataRetrievalAction(None))
      val result = testSpecificStubbedController.onPageLoad(NormalMode).apply(fakeRequest)
      status(result) mustEqual OK
       // contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages).toString

    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {
      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)
      val fakePostRequest = FakeRequest(POST, contactPreferencesRoute).withFormUrlEncodedBody(("value[0]", ContactPreferences.values.head.toString))
      val view = injector.instanceOf[ContactPreferencesView]
      val testSpecificStubbedController = createController(new FakeNavigator(onwardRoute),new FakeDataRetrievalAction(None))
      val result = testSpecificStubbedController.onSubmit(NormalMode).apply(fakePostRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual onwardRoute.url

    }
  }
}
