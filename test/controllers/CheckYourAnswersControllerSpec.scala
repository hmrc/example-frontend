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
import models.{Address, ContactPreferences, PlaceOfBirth}
import pages.{AddressPage, ContactPreferencesPage, DateOfBirthPage, EventNamePage, NameChangePage, NumberOfPropertiesPage, PlaceOfBirthPage}
import play.api.i18n.Messages
import play.api.test.FakeRequest
import play.api.test.Helpers._
import viewmodels.checkAnswers.{AddressSummary, ContactPreferencesSummary, DateOfBirthSummary, EventNameSummary, NameChangeSummary, NumberOfPropertiesSummary, PlaceOfBirthSummary}
import viewmodels.govuk.SummaryListFluency
import views.html.CheckYourAnswersView

import java.time.LocalDate

class CheckYourAnswersControllerSpec extends SpecBase with SummaryListFluency {

  "Check Your Answers Controller" - {

    "must return OK and the correct view for a GET with all required data" in {

      val cp: Set[ContactPreferences] = Set(ContactPreferences.Email)
      val dob: LocalDate = LocalDate.now()
      val address: Address = Address("1 Street", "AA1 1AA")

      val userAnswers = emptyUserAnswers
        .set(ContactPreferencesPage, cp).success.value
        .set(DateOfBirthPage, dob).success.value
        .set(PlaceOfBirthPage, PlaceOfBirth.England).success.value
        .set(NumberOfPropertiesPage, 25).success.value
        .set(NameChangePage, false).success.value
        .set(EventNamePage, "Really good event").success.value
        .set(AddressPage, address).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)
        val result = route(application, request).value
        val view = application.injector.instanceOf[CheckYourAnswersView]
        implicit val msgs: Messages = messages(application)

        val expectedSummaryListItems = Seq(
          ContactPreferencesSummary.row(userAnswers),
          DateOfBirthSummary.row(userAnswers),
          PlaceOfBirthSummary.row(userAnswers),
          NumberOfPropertiesSummary.row(userAnswers),
          NameChangeSummary.row(userAnswers),
          EventNameSummary.row(userAnswers),
          AddressSummary.row(userAnswers)
        ).flatten

        val list = SummaryListViewModel(expectedSummaryListItems)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(list)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad.url)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
