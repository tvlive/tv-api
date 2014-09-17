import infrastructure.AuditFilter
import play.api.mvc.WithFilters

object Global extends WithFilters(AuditFilter)
