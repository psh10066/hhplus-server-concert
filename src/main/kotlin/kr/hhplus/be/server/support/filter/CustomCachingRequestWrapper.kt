package kr.hhplus.be.server.support.filter

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.util.StreamUtils
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

class CustomCachingRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val cachedContent = StreamUtils.copyToByteArray(request.inputStream)

    override fun getInputStream(): ServletInputStream {
        return object : ServletInputStream() {
            private val cachedBodyInputStream: InputStream = ByteArrayInputStream(cachedContent)

            override fun isFinished(): Boolean {
                try {
                    return cachedBodyInputStream.available() == 0
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return false
            }

            override fun isReady(): Boolean {
                return true
            }

            override fun setReadListener(readListener: ReadListener) {
                throw UnsupportedOperationException()
            }

            @Throws(IOException::class)
            override fun read(): Int {
                return cachedBodyInputStream.read()
            }
        }
    }
}