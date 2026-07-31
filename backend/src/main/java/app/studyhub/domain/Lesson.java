package app.studyhub.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="lessons")
public class Lesson {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false)
    private String title;

    private boolean completed = false;

    @ManyToOne(optional=false)
    @JoinColumn(name="course_id")
    private Course course;

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
