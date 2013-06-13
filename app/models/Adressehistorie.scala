package models

import org.joda.time.DateTime
import AnormExtension._
import anorm.SqlParser._
import anorm.~

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 * @since 2.47
 */
case class Adressehistorie(id: Option[Long], adresse: Adresse, person: Person, fra: Option[DateTime], til: Option[DateTime])

object Adressehistorie {
  val adresseHistorie = {
    get[Option[Long]]("id") ~
      get[Long]("adresseId") ~
      get[Long]("personId") ~
      get[Option[DateTime]]("fra") ~
      get[Option[DateTime]]("til") map {
      case id~adresseId~personId~fra~til=> Adressehistorie(id, Adresse.finn(adresseId), Person.finn(personId), fra, til)
    }
  }
}


//TODO: Skrive om klienten til å sende inn adresse-registrering i en operasjon og personregistrering i en annen?