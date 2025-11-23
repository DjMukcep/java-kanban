import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.gym.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public class TimetableTest {

    private Timetable timetable;
    private static Group childGroup, adultGroup;
    private static Coach coach1,couch2;

    @BeforeAll
    static void beforeAll() {
        childGroup = new Group("Акробатика для детей", Age.CHILD, Duration.ofMinutes(60));
        adultGroup = new Group("Акробатика для взрослых", Age.ADULT, Duration.ofMinutes(90));
        coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        couch2 = new Coach("Демидова","Анастасия", "Олеговна");
    }

    @BeforeEach
    void setUp() {
        timetable = new Timetable();
    }

    @Test
    void shouldReturnSingleDaySession() {
        TrainingSession single = new TrainingSession(
                childGroup, coach1, DayOfWeek.MONDAY, LocalTime.of(13,0));

        timetable.addSession(single);
        //Проверить, что за понедельник вернулось одно занятие
        //Проверить, что за вторник не вернулось занятий
        assertEquals(1,timetable.getDaySessions(DayOfWeek.MONDAY).size());
        assertEquals(0,timetable.getDaySessions(DayOfWeek.TUESDAY).size());
    }

    @Test
    void shouldReturnMultipleDaySessions() {
        TrainingSession thursdayAdult = new TrainingSession(adultGroup, coach1,
                DayOfWeek.THURSDAY, LocalTime.of(20,0));
        TrainingSession thursdayChild = new TrainingSession(childGroup, coach1,
                DayOfWeek.THURSDAY, LocalTime.of(13,0));

        timetable.addSession(thursdayAdult);
        timetable.addSession(thursdayChild);

        TrainingSession mondayChild= new TrainingSession(childGroup, coach1,
                DayOfWeek.MONDAY, LocalTime.of(13,0));
        TrainingSession saturdayChild = new TrainingSession(childGroup, coach1,
                DayOfWeek.SATURDAY, LocalTime.of(10,0));

        timetable.addSession(mondayChild);
        timetable.addSession(saturdayChild);
        // Проверить, что за понедельник вернулось одно занятие
        // Проверить, что за вторник не вернулось занятий
        assertEquals(1,timetable.getDaySessions(DayOfWeek.MONDAY).size());
        assertEquals(0,timetable.getDaySessions(DayOfWeek.TUESDAY).size());

        // Проверить, что за четверг вернулось два занятия в ожидаемом порядке: сначала в 13:00, потом в 20:00
        assertEquals(LocalTime.of(13,0),
                timetable.getDaySessions(DayOfWeek.THURSDAY).keySet().toArray()[0]);
        assertEquals(LocalTime.of(20,0),
                timetable.getDaySessions(DayOfWeek.THURSDAY).keySet().toArray()[1]);
    }

    @Test
    void shouldReturnSingleTimeSession() {
        TrainingSession mondayChild= new TrainingSession(childGroup, coach1,
                DayOfWeek.MONDAY, LocalTime.of(13,0));

        timetable.addSession(mondayChild);
        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertEquals(1,timetable.getTimeSessions(DayOfWeek.MONDAY,LocalTime.of(13,0)).size());
        assertEquals(0,timetable.getTimeSessions(DayOfWeek.MONDAY,LocalTime.of(14,0)).size());
    }

    @Test
    void shouldReturnTwoTimeSessions() {
        TrainingSession thursdayAdult = new TrainingSession(adultGroup, coach1,
                DayOfWeek.THURSDAY, LocalTime.of(15,30));
        TrainingSession thursdayChild = new TrainingSession(childGroup, coach1,
                DayOfWeek.THURSDAY, LocalTime.of(15,30));

        timetable.addSession(thursdayAdult);
        timetable.addSession(thursdayChild);

        //проверить что вернулось 2 занятия на конкретное время
        assertEquals(2,timetable.getTimeSessions(DayOfWeek.THURSDAY,LocalTime.of(15,30)).size());
    }

    @Test
    void shouldReturnRightCouchCounterByOneSession() {
        TrainingSession thursdayAdult = new TrainingSession(adultGroup, coach1,
                DayOfWeek.THURSDAY, LocalTime.of(15,30));
        timetable.addSession(thursdayAdult);

        assertEquals(1,timetable.getCountByCoaches().getFirst().getValue());
    }

    @Test
    void shouldReturnRightCouchCounterByTwoSessions() {
        TrainingSession thursdayAdult = new TrainingSession(adultGroup, couch2,
                DayOfWeek.THURSDAY, LocalTime.of(15,30));
        TrainingSession thursdayChild = new TrainingSession(childGroup, couch2,
                DayOfWeek.THURSDAY, LocalTime.of(8,30));

        timetable.addSession(thursdayChild);
        timetable.addSession(thursdayAdult);

        assertEquals(2,timetable.getCountByCoaches().getFirst().getValue());
    }

    @Test
    void shouldReturnMultipleCouchSessionsInExpectedOrder() {
        TrainingSession mondayAdult = new TrainingSession(adultGroup, coach1,
                DayOfWeek.MONDAY, LocalTime.of(15,30));

        timetable.addSession(mondayAdult);

        TrainingSession thursdayAdult = new TrainingSession(adultGroup, couch2,
                DayOfWeek.THURSDAY, LocalTime.of(15,30));
        TrainingSession thursdayChild = new TrainingSession(childGroup, couch2,
                DayOfWeek.THURSDAY, LocalTime.of(8,30));

        timetable.addSession(thursdayChild);
        timetable.addSession(thursdayAdult);

        assertEquals(couch2,timetable.getCountByCoaches().getFirst().getKey());
        assertEquals(coach1,timetable.getCountByCoaches().getLast().getKey());
    }
}
