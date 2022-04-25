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

import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryPlaceOfBirthPage: Arbitrary[PlaceOfBirthPage.type] =
    Arbitrary(PlaceOfBirthPage)

  implicit lazy val arbitraryNumberOfPropertiesPage: Arbitrary[NumberOfPropertiesPage.type] =
    Arbitrary(NumberOfPropertiesPage)

  implicit lazy val arbitraryNameChangePage: Arbitrary[NameChangePage.type] =
    Arbitrary(NameChangePage)

  implicit lazy val arbitraryEventNamePage: Arbitrary[EventNamePage.type] =
    Arbitrary(EventNamePage)

  implicit lazy val arbitraryDateOfBirthPage: Arbitrary[DateOfBirthPage.type] =
    Arbitrary(DateOfBirthPage)

  implicit lazy val arbitraryAddressPage: Arbitrary[AddressPage.type] =
    Arbitrary(AddressPage)

  implicit lazy val arbitraryContactPreferencesPage: Arbitrary[ContactPreferencesPage.type] =
    Arbitrary(ContactPreferencesPage)
}
