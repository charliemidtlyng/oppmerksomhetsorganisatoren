package models

import org.joda.time.DateTime

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 * @since 2.47
 */
case class Adressehistorie(id: Option[Long], adresseId: Long, personId: Long, fra: DateTime, til: DateTime) {

}
