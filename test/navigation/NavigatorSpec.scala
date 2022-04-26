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

package navigation

import base.SpecBase
import controllers.routes
import pages._
import models._

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, NormalMode, UserAnswers("id")) mustBe routes.IndexController.onPageLoad
      }

      "must go from the ContactPreferencePage to DateOfBirthPage " in {
        navigator.nextPage(
          ContactPreferencesPage, NormalMode, UserAnswers("id")
        ) mustBe routes.DateOfBirthController.onPageLoad(NormalMode)
      }

      "must go from the DateOfBirthPage to PlaceOfBirthPage" in {
        navigator.nextPage(
          DateOfBirthPage, NormalMode, UserAnswers("id")
        ) mustBe routes.PlaceOfBirthController.onPageLoad(NormalMode)
      }

      "must go from the PlaceOfBirthPage to NumberOfPropertiesPage" in {
        navigator.nextPage(
          PlaceOfBirthPage, NormalMode, UserAnswers("id")
        ) mustBe routes.NumberOfPropertiesController.onPageLoad(NormalMode)
      }

      "must go from the NumberOfPropertiesPage to NameChangePage" in {
        navigator.nextPage(
          NumberOfPropertiesPage, NormalMode, UserAnswers("id")
        ) mustBe routes.NameChangeController.onPageLoad(NormalMode)
      }

      "must go from the NameChangePage to EventNamePage" in {
        navigator.nextPage(
          NameChangePage, NormalMode, UserAnswers("id")
        ) mustBe routes.EventNameController.onPageLoad(NormalMode)
      }

      "must go from the EventNamePage to AddressPage" in {
        navigator.nextPage(
          EventNamePage, NormalMode, UserAnswers("id")
        ) mustBe routes.AddressController.onPageLoad(NormalMode)
      }

      "must go from the AddressPage to CheckYourAnswers" in {
        navigator.nextPage(
          AddressPage, NormalMode, UserAnswers("id")
        ) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad
      }
    }
  }
}
