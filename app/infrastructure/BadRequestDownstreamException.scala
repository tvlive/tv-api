package infrastructure

case object BadRequestDownstreamException extends Exception
case object InternalServerErrorDownstreamException extends Exception
case object InvalidTokenException extends Exception
