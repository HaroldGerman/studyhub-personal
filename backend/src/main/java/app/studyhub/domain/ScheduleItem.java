package app.studyhub.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="schedule_items")
public class ScheduleItem {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false)
    private String dayOfWeek; // "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"

    @Column(nullable=false)
    private String startTime; // e.g. "08:00"

    @Column(nullable=false)
    private String endTime; // e.g. "11:00"

    @Column(nullable=false)
    private String courseTitle; // e.g. "Estudio: Data Engineer"

    private String color = "#7257e8";

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
