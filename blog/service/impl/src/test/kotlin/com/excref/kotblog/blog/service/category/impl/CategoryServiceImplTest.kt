package com.excref.kotblog.blog.service.category.impl

import com.excref.kotblog.blog.persistence.category.CategoryRepository
import com.excref.kotblog.blog.service.category.CategoryService
import com.excref.kotblog.blog.service.category.domain.Category
import com.excref.kotblog.blog.service.category.exception.CategoryAlreadyExistsForNameException
import com.excref.kotblog.blog.service.category.exception.CategoryNotExistsForUuidException
import com.excref.kotblog.blog.service.test.AbstractServiceImplTest
import org.assertj.core.api.Assertions.assertThat
import org.easymock.EasyMock
import org.easymock.EasyMock.expect
import org.easymock.EasyMock.isA
import org.easymock.Mock
import org.easymock.TestSubject
import org.junit.Test
import java.util.*

/**
 * @author Ruben Vardanyan
 * @since 06/07/2017 12:42
 */
class CategoryServiceImplTest : AbstractServiceImplTest() {

    //region Test subject and mocks
    @TestSubject
    private val categoryService: CategoryService = CategoryServiceImpl()

    @Mock
    private lateinit var categoryRepository: CategoryRepository
    //endregion

    //region initial
    @Test
    fun testCategoryService() {
        assertThat(categoryService).isNotNull()
    }

    @Test
    fun testCategoryRepository() {
        assertThat(categoryRepository).isNotNull()
    }
    //endregion

    //region create

    /**
     *  When category with name already exists
     */
    @Test
    fun create1() {
        resetAll()
        // test data
        val name = "Software development"
        val category = helper.buildCategory(name)
        //expectations
        expect(categoryRepository.findByName(name)).andReturn(category)
        replayAll()
        // test scenario
        try {
            categoryService.create(name)
        } catch (ex: CategoryAlreadyExistsForNameException) {
            assertThat(ex).isNotNull().extracting("name").containsOnly(name)
        }
        verifyAll()
    }

    @Test
    fun create2() {
        resetAll()
        // test data
        val name = "Software development"
        //expectations
        expect(categoryRepository.findByName(name)).andReturn(null)
        expect(categoryRepository.save(isA(Category::class.java))).andAnswer({ EasyMock.getCurrentArguments()[0] as Category? })
        replayAll()
        // test scenario
        val category = categoryService.create(name)
        assertThat(category).isNotNull().extracting("name").containsOnly(name)
        verifyAll()
    }
    //endregion

    //region getByUuid
    /**
     * When does not exists for uuid
     */
    @Test
    fun getByUuid1() {
        resetAll()
        // test data
        val uuid = UUID.randomUUID().toString()
        // expectations
        expect(categoryRepository.findByUuid(uuid)).andReturn(null)
        replayAll()
        // test scenario
        try {
            categoryService.getByUuid(uuid)
        } catch (ex: CategoryNotExistsForUuidException) {
            assertThat(ex).isNotNull().extracting("uuid").containsOnly(uuid)
        }
        verifyAll()
    }

    @Test
    fun getByUuid2() {
        resetAll()
        // test data
        val category = helper.buildCategory()
        val uuid = category.uuid
        // expectations
        expect(categoryRepository.findByUuid(uuid)).andReturn(category)
        replayAll()
        // test scenario
        val result = categoryService.getByUuid(uuid)
        assertThat(result).isNotNull().isEqualTo(category)
        verifyAll()
    }
    //endregion

    //region existsForName
    /**
     * When does not exist
     */
    @Test
    fun existsForName1() {
        resetAll()
        // test data
        val name = "Art"
        // expectations
        expect(categoryRepository.findByName(name)).andReturn(null)
        replayAll()
        // test scenario
        assertThat(categoryService.existsForName(name)).isFalse()
    }

    /**
     * When exists
     */
    @Test
    fun existsForName2() {
        resetAll()
        // test data
        val name = "Sports"
        val category = helper.buildCategory(name)
        // expectations
        expect(categoryRepository.findByName(name)).andReturn(category)
        replayAll()
        // test scenario
        assertThat(categoryService.existsForName(name)).isTrue()
    }
    //endregion

}