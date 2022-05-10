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

import base.SpecBase
import controllers.actions.{DataRequiredAction, FakeDataRetrievalAction, FakeIdentifierAction}
import forms.ContactPreferencesFormProvider
import models.{ContactPreferences, NormalMode, UserAnswers}
import navigation.Navigator
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import pages.ContactPreferencesPage
import play.api.http.Status.{BAD_REQUEST, OK, SEE_OTHER}
import play.api.mvc.{Call, ControllerComponents}
import play.api.test.Helpers.baseApplicationBuilder.injector
import play.api.test.Helpers.{GET, POST, defaultAwaitTimeout, redirectLocation, status}
import play.api.test.{FakeRequest, Helpers}
import repositories.SessionRepository
import views.html.ContactPreferencesView

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ContactPreferencesControllerSpec2 extends SpecBase with BeforeAndAfterEach with MockitoSugar {
  val cc: ControllerComponents = Helpers.stubControllerComponents()

  private val nonEmptyUserAnswers = Some(emptyUserAnswers.set(ContactPreferencesPage, ContactPreferences.values.toSet).success.value)
  private val contactPreferencesRoute = routes.ContactPreferencesController.onPageLoad(NormalMode).url
  private val view = injector.instanceOf[ContactPreferencesView]
  private val fakeIdentifierAction = new FakeIdentifierAction(cc.parsers)
  private val fakeGetRequest = FakeRequest(GET, contactPreferencesRoute)
  private val fakePostRequest = FakeRequest(POST, contactPreferencesRoute)
  private val contactPreferencesFormProvider = new ContactPreferencesFormProvider()
  private val mockSessionRepository = mock[SessionRepository]
  private val mockNavigator = mock[Navigator]
  private val mockDataRequiredAction = mock[DataRequiredAction]
  private val stubbedControllerDefault = createController(Some(emptyUserAnswers))

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockSessionRepository, mockNavigator,mockDataRequiredAction)
  }

  def onwardRoute = Call("GET", "/foo")

  private def createController(userAnswers: Option[UserAnswers]) = {
    new ContactPreferencesController(
      Helpers.stubMessagesApi(),
      mockSessionRepository,
      mockNavigator,
      fakeIdentifierAction,
      new FakeDataRetrievalAction(userAnswers),
      mockDataRequiredAction,
      contactPreferencesFormProvider,
      Helpers.stubMessagesControllerComponents(), view
    )
  }

  "ContactPreferences Controller" - {
    "must return OK for a GET request" in {
      val result = stubbedControllerDefault.onPageLoad(NormalMode).apply(fakeGetRequest)
      status(result) mustBe OK
    }

    "must return OK for a GET request when the question has previously been answered" in {
      val result = createController(nonEmptyUserAnswers).onPageLoad(NormalMode).apply(fakeGetRequest)
      status(result) mustEqual OK
    }

    "must redirect to the next page when valid data is submitted" in {
      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)
      when(mockNavigator.nextPage(any(),any(),any())) thenReturn onwardRoute
      val result = createController(nonEmptyUserAnswers).onSubmit(NormalMode)
        .apply(fakePostRequest.withFormUrlEncodedBody(("value[0]", ContactPreferences.values.head.toString)))

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual onwardRoute.url
    }

    "must return a Bad Request, when invalid data is submitted" in {
      val result = stubbedControllerDefault.onSubmit(NormalMode)
        .apply(fakePostRequest.withFormUrlEncodedBody(("value", "invalid value")))
      status(result) mustEqual BAD_REQUEST
    }

    "must correctly load the page for a GET request if no existing data is found" in {
      val result = stubbedControllerDefault.onPageLoad(NormalMode).apply(fakeGetRequest)
      status(result) mustEqual OK
    }

    "must redirect to Journey Recovery for a POST request if no existing data is found" in {
      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)
      when(mockNavigator.nextPage(any(),any(),any())) thenReturn onwardRoute
      val result = stubbedControllerDefault.onSubmit(NormalMode)
        .apply(fakePostRequest.withFormUrlEncodedBody(("value[0]", ContactPreferences.values.head.toString)))

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual onwardRoute.url
    }
  }
}
