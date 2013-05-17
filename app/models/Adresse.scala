package models
import anorm._
import anorm.SqlParser._
import AnormExtension._
import play.api.db._
import play.api.Play.current
import play.api.data.FormError
import play.api.data.format.Formatter
import play.api.libs.json._
import anorm.~
import play.api.libs.json.JsObject

case class Adresse(id:Option[Long], adressenavn: String, postnummer: String, poststed: String)

  object Adresse {
  val adresse = {
    get[Option[Long]]("id") ~
      get[String]("adressenavn") ~
      get[String]("postnummer") ~
      get[String]("poststed") map {
      case id ~ adressenavn ~ postnummer ~ poststed => Adresse(id, adressenavn, postnummer, poststed)
    }
  }

  def all(): List[Adresse] = DB.withConnection {
    implicit c =>
      SQL("select * from adresse").as(adresse *)
  }


  def opprett(adresse: Adresse)= {
    DB.withConnection {
      implicit c =>
        SQL("insert into adresse (adressenavn, postnummer, poststed) values ({adressenavn}, {postnummer}, {poststed})").on(
          'adressenavn -> adresse.adressenavn,
          'postnummer -> adresse.postnummer,
          'poststed -> adresse.poststed
        ).executeInsert()
    }
  }

  def slett(id: Long) {
    DB.withConnection {
      implicit c =>
        SQL("delete from adresse where id = {id}").on(
          'id -> id
        ).executeUpdate()
    }

  }

  def finn(id: Long): Adresse = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from adresse where id = {id}").on("id" -> id).as(Adresse.adresse.single)
    }
  }


    implicit val addresseFormat = Json.format[Adresse]

  }