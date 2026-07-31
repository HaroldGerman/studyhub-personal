package app.studyhub.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="courses")
public class Course {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable=false)
    private String title;

    private String code;
    private String description;
    private String professor;
    private String university;
    private String platform;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.NOT_STARTED;

    private String color = "#6750E0";
    private String icon = "✦";

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
