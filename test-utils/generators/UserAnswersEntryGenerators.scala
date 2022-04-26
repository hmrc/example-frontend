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

package generators

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryPlaceOfBirthUserAnswersEntry: Arbitrary[(PlaceOfBirthPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PlaceOfBirthPage.type]
        value <- arbitrary[PlaceOfBirth].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNumberOfPropertiesUserAnswersEntry: Arbitrary[(NumberOfPropertiesPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[NumberOfPropertiesPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNameChangeUserAnswersEntry: Arbitrary[(NameChangePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[NameChangePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryEventNameUserAnswersEntry: Arbitrary[(EventNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[EventNamePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDateOfBirthUserAnswersEntry: Arbitrary[(DateOfBirthPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DateOfBirthPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddressUserAnswersEntry: Arbitrary[(AddressPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddressPage.type]
        value <- arbitrary[Address].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryContactPreferencesUserAnswersEntry: Arbitrary[(ContactPreferencesPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ContactPreferencesPage.type]
        value <- arbitrary[ContactPreferences].map(Json.toJson(_))
      } yield (page, value)
    }
}
