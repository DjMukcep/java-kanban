package ru.yandex.practicum.gym;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record TrainingSession(
        Group group, Coach coach, DayOfWeek dayOfWeek, LocalTime timeOfDay) {
}
