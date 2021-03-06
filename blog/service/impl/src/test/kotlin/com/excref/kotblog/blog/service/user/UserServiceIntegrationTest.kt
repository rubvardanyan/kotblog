package com.excref.kotblog.blog.service.user

import com.excref.kotblog.blog.service.test.AbstractServiceIntegrationTest
import com.excref.kotblog.blog.service.user.domain.UserRole
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

/**
 * @author Arthur Asatryan
 * @since 6/8/17 1:21 AM
 */
@EnableAutoConfiguration
class UserServiceIntegrationTest : AbstractServiceIntegrationTest() {

    //region Dependencies
    @Autowired
    private lateinit var userService: UserService
    //endregion

    //region Test methods
    @Test
    fun testCreate() {
        // given
        val email = "biacoder@gmail.com"
        val password = "you can't even guess me! :P"
        val role = UserRole.BLOGGER
        // when
        val result = userService.create(email, password, role)
        // then
        assertThat(result).isNotNull().extracting("email", "password", "role").containsOnly(email, password, role)
    }

    @Test
    fun testExistsForEmail() {
        // given
        val email = "biacoder@gmail.com"
        helper.persistUser(email = email)
        // when
        val existsForEmail = userService.existsForEmail(email)
        assertThat(existsForEmail).isTrue()
    }
    //endregion
}