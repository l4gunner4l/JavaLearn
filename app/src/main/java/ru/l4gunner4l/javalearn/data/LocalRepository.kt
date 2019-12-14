package ru.l4gunner4l.javalearn.data

import ru.l4gunner4l.javalearn.models.Lesson

object LocalRepository {

    var lessons = listOf(
            Lesson(1, "Hello World", true, 2),
            Lesson(2, "String", true, 1),
            Lesson(3, "Button", true, 0),
            Lesson(3, "Button", true, 0),
            Lesson(3, "Button", true, 0),
            Lesson(3, "Button", true, 0),
            Lesson(3, "Button", true, 0),
            Lesson(3, "Button", true, 0),
            Lesson(3, "Button", true, 0),
            Lesson(3, "Button", true, 0),
            Lesson(3, "Button", true, 0),
            Lesson(4, "Int", true, 3),
            Lesson(5, "println", false, null),
            Lesson(6, "for/while", false, null)
    )

}