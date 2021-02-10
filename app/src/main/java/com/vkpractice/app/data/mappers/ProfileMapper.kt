package com.vkpractice.app.data.mappers

import com.vkpractice.app.data.database.entities.ProfileModel
import com.vkpractice.app.data.network.responses.CareerItem
import com.vkpractice.app.data.network.responses.ProfileResponseItem
import javax.inject.Inject

/**
 * @autor d.snytko
 */
class ProfileMapper @Inject constructor() {

    operator fun invoke(
        profileItems: List<ProfileResponseItem>?,
        groupsCount: Int?,
        friendsCount: Int?,
        postsCount: Int?,
        profileCareer: MutableList<CareerItem>
    ): List<ProfileModel> = profileItems?.map { profile ->
        ProfileModel(
            id = profile.id,
            firstName = profile.firstName,
            lastName = profile.lastName,
            avatar = profile.photoMax,
            country = profile.country?.title,
            city = profile.city?.title,
            lastSeen = profile.lastSeen.time * 1000L,
            birthday = profile.bdate,
            about = profile.about,
            universityName = profile.universityName,
            quotes = profile.quotes,
            faculty = profile.faculty,
            photoMaxOrig = profile.photoMaxOrig,
            facultyName = profile.facultyName,
            screenName = profile.screenName,
            graduation = profile.graduation,
            domain = profile.domain,
            nickname = profile.nickname,
            online = profile.online,
            friendsCount = friendsCount,
            postCount = postsCount,
            groupsCount = groupsCount,
            profileCareer = profileCareer
        )
    } ?: emptyList()
}
