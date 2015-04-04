package utils

import models.TVContent

trait TVContentSorter {

  def sortedBy: (TVContent, TVContent) => Boolean = {
    (f, s) => (f.rating, s.rating) match {
      case (Some(fr), Some(sr)) if fr > sr => true
      case (Some(fr), Some(sr)) if fr == sr && f.start.isBefore(s.start) => true
      case (Some(fr), Some(sr)) if fr == sr && f.start.isEqual(s.start) && f.channel <= s.channel => true
      case (Some(fr), None) => true
      case (None, None) if f.start.isBefore(s.start) => true
      case (None, None) if f.start.isEqual(s.start) && f.channel <= s.channel => true
      case _ => false
    }
  }
}
