package app.studyhub.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="notes")
public class Note {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false, columnDefinition="TEXT")
    private String body;

    private LocalDateTime lastModified = LocalDateTime.now();

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;

    private UUID courseId; // Optional link to a course
    private UUID lessonId; // Optional link to a lesson

    @Column(columnDefinition="TEXT")
    private String scratchpad = "";

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getScratchpad() { return scratchpad; }
    public void setScratchpad(String scratchpad) { this.scratchpad = scratchpad; }
    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public UUID getCourseId() { return courseId; }
    public void setCourseId(UUID courseId) { this.courseId = courseId; }
    public UUID getLessonId() { return lessonId; }
    public void setLessonId(UUID lessonId) { this.lessonId = lessonId; }
}
