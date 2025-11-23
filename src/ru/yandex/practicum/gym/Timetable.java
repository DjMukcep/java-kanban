package ru.yandex.practicum.gym;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public final class Timetable {

    private final Map<DayOfWeek, Map<LocalTime,List<TrainingSession>>> weekSessions = new HashMap<>();
    private final Map<Coach,Integer> coachSessions = new HashMap<>();

    public Timetable() {
        Arrays.stream(DayOfWeek.values()).forEach(day -> weekSessions.put(day,new TreeMap<>()));
    }

    public void addSession(TrainingSession session) {
        Map<LocalTime,List<TrainingSession>> byTimeList = weekSessions.get(session.dayOfWeek());
        List<TrainingSession> list = byTimeList.getOrDefault(session.timeOfDay(),new ArrayList<>());
        list.add(session);
        byTimeList.put(session.timeOfDay(),list);
        weekSessions.put(session.dayOfWeek(),byTimeList);

        int counter = coachSessions.getOrDefault(session.coach(),0);
        coachSessions.put(session.coach(),Math.incrementExact(counter));
    }

    public List<Map.Entry<Coach,Integer>> getCountByCoaches() {
        return coachSessions.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).toList();
    }

    public Map<LocalTime,List<TrainingSession>> getDaySessions(DayOfWeek day) {
        return weekSessions.get(day);
    }

    public List<TrainingSession> getTimeSessions(DayOfWeek day, LocalTime time) {
        return weekSessions.get(day).getOrDefault(time,List.of());
    }
}
