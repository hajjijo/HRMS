package models

import org.joda.time.DateTime

case class DateTest (
                      date: DateTime,
                      id: Option[Long] = None
                    )