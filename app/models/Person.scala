package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import org.joda.time.DateTime
import AnormExtension._
import play.api.libs.json.Json


case class Person(id: Option[Long], fodselsdato: DateTime, adresse: Adresse, info: String )

object Person {

  val person = {
    get[Option[Long]]("id") ~
      get[DateTime]("fodselsdato") ~
      get[Long]("adresseId") ~
      get[String]("info") map {
      case id~fodselsdato~adresse~info=> Person(id, fodselsdato, Adresse.finn(adresse), info)
    }
  }

  def all(): List[Person] = DB.withConnection { implicit c =>
    SQL("select * from person").as(person *)
  }


  def opprett(p:Person) = {
    DB.withConnection { implicit c =>
      SQL("insert into person (fodselsdato, adresseId, info) values ({fodselsdato}, {adresseId}, {info})").on(
        'fodselsdato -> p.fodselsdato,
        'adresseId-> p.adresse.id,
        'info-> p.info
      ).executeInsert()
    }
  }

  def slett(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from person where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }

  }
  implicit val personFormat = Json.format[Person]
}
