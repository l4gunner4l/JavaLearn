package ru.l4gunner4l.javalearn.data

import ru.l4gunner4l.javalearn.models.Lesson

object LocalRepository {

    var lessons = listOf(
            Lesson(1, "Hello World"),
            Lesson(2, "String"),
            Lesson(3, "Button"),
            Lesson(4, "Button"),
            Lesson(5, "Button"),
            Lesson(6, "Button"),
            Lesson(7, "Button")
    )

}