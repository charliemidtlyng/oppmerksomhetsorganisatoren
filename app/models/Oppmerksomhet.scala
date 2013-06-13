package models

import org.joda.time.DateTime
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import AnormExtension._
import play.api.db.DB
import special.Superoperators
import models.enums.{EnumUtils, Rolle, Hendelsestype, Involverttype}
import models.enums.EnumUtils._
import play.api.libs.json.{Reads, Json}
import se.radley.plugin.enumeration._
import play.api.libs.json._


/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 * @since 2.47
 */
case class Oppmerksomhet(id: Option[Long],
                         til: Option[Involvert],
                         tilType: Option[models.enums.Involverttype.Value],
                         fra: Option[Involvert],
                         fraType: Option[models.enums.Involverttype.Value],
                         url: Option[String],
                         info: Option[String],
                         verdi: Option[Double],
                         tid: DateTime,
                         hendelsestype: models.enums.Hendelsestype.Value,
                         levert: Boolean,
                         rolle: models.enums.Rolle.Value)

object Oppmerksomhet {

  def utledInvolvert(involvertId: Option[Long], involvertTypeStreng: Option[String]): Option[Involvert] = {
    val involvertType = utledInvolvertType(involvertTypeStreng)
    involvertType match {
      case Some(Involverttype.Familie) => Option(Adresse.finn(involvertId.get))
      case Some(Involverttype.Person) => Option(Person.finn(involvertId.get))
      case _ => None
    }
  }

  def utledInvolvertType(involvertType: Option[String]): Option[Involverttype.Value] = {

    val typetekst:String = if(involvertType.getOrElse("").isEmpty) "Ingen" else involvertType.get
    Involverttype.withName(typetekst) match {
      case Involverttype.Familie => Option(Involverttype.Familie)
      case Involverttype.Person => Option(Involverttype.Person)
      case _ => Option(Involverttype.Ingen)
    }
  }

  val oppmerksomhet = {
    get[Option[Long]]("id") ~
      get[Option[Long]]("til_id") ~
      get[Option[String]]("tilType") ~
      get[Option[Long]]("fra_id") ~
      get[Option[String]]("fraType") ~
      get[Option[String]]("url") ~
      get[Option[String]]("info") ~
      get[Option[Double]]("verdi") ~
      get[DateTime]("tid") ~
      get[String]("hendelsestype") ~
      get[Boolean]("levert") ~
      get[String]("rolle") map {
      case id ~ til_id ~ tilType ~ fra_id ~ fraType ~ url ~ info ~ verdi ~ tid ~ hendelsestype ~ levert ~ rolle =>
        Oppmerksomhet(id, utledInvolvert(til_id, tilType), utledInvolvertType(tilType), utledInvolvert(fra_id, fraType), utledInvolvertType(fraType), url, info, verdi, tid, Hendelsestype.withName(hendelsestype), levert, Rolle.withName(rolle))
    }
  }

  def opprett(o: Oppmerksomhet) = {
    DB.withConnection {
      implicit c =>
        SQL("insert into oppmerksomhet (til_id,tilType, fra_id, fraType, url, info, verdi, tid, hendelsestype, levert, rolle) values ({til}, {tilType}, {fra}, {fraType}, {url}, {info}, {verdi}, {tid}, {hendelsestype}, {levert}, {rolle})").on(
          'til -> Superoperators.?(o.til.get.id),
          'tilType -> o.tilType.getOrElse("").toString,
          'fra -> Superoperators.?(o.fra.get.id),
          'fraType -> o.fraType.getOrElse("").toString,
          'url -> o.url.getOrElse(""),
          'info -> o.info.getOrElse(""),
          'verdi -> o.verdi.getOrElse(0),
          'tid -> o.tid,
          'hendelsestype -> o.hendelsestype.toString,
          'levert -> o.levert,
          'rolle -> o.rolle.toString
        ).executeInsert()
    }
  }

  def finn(id: Long): Oppmerksomhet = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from oppmerksomhet where id = {id}").on("id" -> id).as(oppmerksomhet.single)
    }
  }


  def all(): List[Oppmerksomhet] = DB.withConnection {
    implicit c =>
      SQL("select * from oppmerksomhet").as(oppmerksomhet *)
  }


//  implicit val involvertWrite = new Writes[Involvert] {
//    def writes(i: Involvert): JsValue = {
//      if(i.isInstanceOf[Person]){
//        Json.toJson(i)
//      } else if(i.isInstanceOf[Adresse]) {
//        Json.toJson(i)
//      } else {
//        Json.obj("id" -> i.id)
//      }
//    }
//  }
//
//  implicit val involvertRead = new Reads[Involvert] {
//    def reads(i: Involvert): JsValue = {
//      if(i.isInstanceOf[Person]){
//        Json.toJson(i)
//      } else if(i.isInstanceOf[Adresse]) {
//        Json.toJson(i)
//      } else {
//        Json.obj("id" -> i.id)
//      }
//    }
//  }

  implicit val involvertFormat = new Format[Involvert] {

    def writes(i: Involvert): JsValue = {
      i match {
        case person:Person => Json.toJson(person)
        case adresse:Adresse => Json.toJson(adresse)
        case _ => Json.obj("id" -> i.id)
      }
    }

    def reads(jv: JsValue): JsResult[Involvert] ={
      val involvertId = (jv \ "id").asOpt[Long]
      val involvert = new Involvert {
        val id: Option[Long] = involvertId
      }
      JsSuccess(involvert)
    }
  }

  implicit val hendelsestypeFormat = EnumUtils.enumFormat(Hendelsestype)
  implicit val rolleFormat = EnumUtils.enumFormat(Rolle)
  implicit val involverttypeFormat = EnumUtils.enumFormat(Involverttype)

  implicit val oppmerksomhetFormat = Json.format[Oppmerksomhet]


  //  id: Option[Long],
//  til: Option[Involvert],
//  tilType: Option[models.enums.Involverttype.Value],
//  fra: Option[Involvert],
//  fraType: Option[models.enums.Involverttype.Value],
//  url: Option[String],
//  info: Option[String],
//  verdi: Option[Double],
//  tid: DateTime,
//  hendelsestype: models.enums.Hendelsestype.Value,
//  levert: Boolean,
//  rolle: models.enums.Rolle.Value

//  implicit val oppmerksomhetFormat = (
//      (__ \ "id").formatNullable[Long] and
//      (__ \ "til").formatNullable[String] and
//      (__ \ "tiltype").format[Long]
//    )((id, name, group) => Oppmerksomhet(id, name, group),
//    (p: Oppmerksomhet) => (Option(p.id), p.til, p.groupId))


}