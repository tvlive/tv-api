package utils

import models.TVContent

trait TVContentSorter {

  def sortedByRatingTimeChannel: (TVContent, TVContent) => Boolean = {
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

  def sortedByTimeRatingChannel: (TVContent, TVContent) => Boolean = {
    (f, s) => (f.start, f.rating, s.start, s.rating) match {
      case (fs, _, ss, _) if fs.isBefore(ss) => true
      case (fs, Some(fr), ss, Some(sr)) if fs.isEqual(ss) && fr > sr => true
      case (fs, Some(fr), ss, None) if fs.isEqual(ss) => true
      case (fs, Some(fr), ss, Some(sr)) if fs.isEqual(ss) && fr == sr && f.channel <= s.channel => true
      case (fs, None, ss, None) if fs.isEqual(ss) && f.channel <= s.channel => true
      case _ => false
    }
  }

}
