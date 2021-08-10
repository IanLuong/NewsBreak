package com.ianluong.newsbreak.ui.newStories

import android.app.Application
import com.ianluong.newsbreak.StoryRepository
import org.junit.Before

import org.junit.Assert.*
import org.mockito.Mockito

class NewStoriesViewModelTest {

    private val applicationMock = Mockito.mock(Application::class.java)
    private val repositoryMock = Mockito.mock(StoryRepository::class.java)

    private lateinit var subject: NewStoriesViewModel
    private lateinit var searchTerm: String

    @Before
    fun setUp() {
        subject = NewStoriesViewModel(applicationMock)
        searchTerm = "BIG"
    }

    fun addingStoryWorks() {
        subject
    }
}