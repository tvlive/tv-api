package infrastructure

import models.AuthorizationInfo
import sun.misc.{BASE64Decoder, BASE64Encoder}

import scala.util.Try
import scala.util.matching.Regex

trait Decryptor {
  private val regEx = new Regex("^(.*):(.*)$", "username", "token")
  protected val decoder: BASE64Decoder
  protected val encoder: BASE64Encoder

  def decodeToken(tokenEncoded: String): Try[AuthorizationInfo] = {
    val decoded = decoder.decodeBuffer(tokenEncoded)
    val decodedAsString = new String(decoded)

    Try(
      regEx findFirstMatchIn decodedAsString match {
        case Some(m) => AuthorizationInfo(m.group("username"), m.group("token"))
        case None => throw InvalidTokenException
      }
    )
  }

  def encode(username: String, token: String) = encoder.encode(s"${username}:${token}".getBytes)
}

object Decryptor extends Decryptor {
  override protected val decoder: BASE64Decoder = new sun.misc.BASE64Decoder()
  override protected val encoder: BASE64Encoder = new sun.misc.BASE64Encoder()
}


//object RSA {
//  def decodePublicKey(encodedKey: String): Option[PublicKey] = {
//    this.decodePublicKey(new Base64().decode(encodedKey))
//  }
//
//  def decodePublicKey(encodedKey: Array[Byte]): Option[PublicKey] = {
//    scala.util.control.Exception.allCatch.opt {
//      val spec = new X509EncodedKeySpec(encodedKey)
//      val factory = KeyFactory.getInstance("RSA")
//      factory.generatePublic(spec)
//    }
//  }
//
//  def encrypt(key: PublicKey, data: Array[Byte]): Array[Byte] = {
//    val cipher = Cipher.getInstance("RSA")
//    cipher.init(Cipher.ENCRYPT_MODE, key)
//    cipher.doFinal(data)
//  }
//
//  def encryptB64(key: PublicKey, data: Array[Byte]): String = {
//    (new Base64()).encodeAsString(this.encrypt(key, data))
//  }
//
//  def encryptB64(key: PublicKey, data: String): String = {
//    this.encryptB64(key, data.getBytes)
//  }
//}

