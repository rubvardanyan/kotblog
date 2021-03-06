package com.excref.kotblog.blog.service.tag.domain

import com.excref.kotblog.blog.service.common.UuidAwareDomain
import javax.persistence.Column
import javax.persistence.Entity

/**
 * @author Arthur Asatryan
 * @since 6/4/17 3:13 PM
 */
@Entity
data class Tag(
        @Column(name = "name", unique = true, nullable = false)
        val name: String
) : UuidAwareDomain()