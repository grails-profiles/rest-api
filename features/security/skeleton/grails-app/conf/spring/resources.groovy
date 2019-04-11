import grails.plugin.springsecurity.BeanTypeResolver
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.LdapShaPasswordEncoder
import org.springframework.security.crypto.password.Md4PasswordEncoder
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.security.crypto.password.StandardPasswordEncoder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import grails.plugin.springsecurity.BeanTypeResolver


beans = {
    /** passwordEncoder */
    def conf = SpringSecurityUtils.securityConfig
    String algorithm = conf.password.algorithm
    Class beanTypeResolverClass = conf.beanTypeResolverClass ?: BeanTypeResolver
    def beanTypeResolver = beanTypeResolverClass.newInstance(conf, application)

    passwordEncoder(beanTypeResolver.resolveType('passwordEncoder', DelegatingPasswordEncoder), algorithm, idToPasswordEncoder(conf))
}


Map<String, PasswordEncoder> idToPasswordEncoder(ConfigObject conf) {

    final String ENCODING_ID_BCRYPT = "bcrypt"
    final String ENCODING_ID_LDAP = "ldap"
    final String ENCODING_ID_MD4 = "MD4"
    final String ENCODING_ID_MD5 = "MD5"
    final String ENCODING_ID_NOOP = "noop"
    final String ENCODING_ID_PBKDF2 = "pbkdf2"
    final String ENCODING_ID_SCRYPT = "scrypt"
    final String ENCODING_ID_SHA1 = "SHA-1"
    final String ENCODING_IDSHA256 = "SHA-256"

    MessageDigestPasswordEncoder messageDigestPasswordEncoderMD5 = new MessageDigestPasswordEncoder(ENCODING_ID_MD5)
    messageDigestPasswordEncoderMD5.encodeHashAsBase64 = conf.password.encodeHashAsBase64 // false
    messageDigestPasswordEncoderMD5.iterations = conf.password.hash.iterations // 10000

    MessageDigestPasswordEncoder messsageDigestPasswordEncoderSHA1 = new MessageDigestPasswordEncoder(ENCODING_ID_SHA1)
    messsageDigestPasswordEncoderSHA1.encodeHashAsBase64 = conf.password.encodeHashAsBase64 // false
    messsageDigestPasswordEncoderSHA1.iterations = conf.password.hash.iterations // 10000

    MessageDigestPasswordEncoder messsageDigestPasswordEncoderSHA256 = new MessageDigestPasswordEncoder(ENCODING_IDSHA256)
    messsageDigestPasswordEncoderSHA256.encodeHashAsBase64 = conf.password.encodeHashAsBase64 // false
    messsageDigestPasswordEncoderSHA256.iterations = conf.password.hash.iterations // 10000

    int strength = conf.password.bcrypt.logrounds
    [(ENCODING_ID_BCRYPT): new BCryptPasswordEncoder(strength),
     (ENCODING_ID_LDAP): new LdapShaPasswordEncoder(),
     (ENCODING_ID_MD4): new Md4PasswordEncoder(),
     (ENCODING_ID_MD5): messageDigestPasswordEncoderMD5,
     (ENCODING_ID_NOOP): NoOpPasswordEncoder.getInstance(),
     (ENCODING_ID_PBKDF2): new Pbkdf2PasswordEncoder(),
     (ENCODING_ID_SCRYPT): new SCryptPasswordEncoder(),
     (ENCODING_ID_SHA1): messsageDigestPasswordEncoderSHA1,
     (ENCODING_IDSHA256): messsageDigestPasswordEncoderSHA256,
     "sha256": new StandardPasswordEncoder()]
}
