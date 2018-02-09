package rollcall.model

import org.joda.time.DateTime

case class RollCallEntity(
                           employ_id: Long,
                           in_date: DateTime,
                           out_date: Option[DateTime] = None,
                           id: Option[Long] = None
                         )
