package com.example.lms.controller;

import com.example.lms.model.Course;
import com.example.lms.repository.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Controller
public class CourseController {

    // Fixed admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private final CourseRepository courseRepository;

    // Sample courses data
    private final List<Course> sampleCourses = Arrays.asList(
            new Course(
                    "Spring Boot Fundamentals",
                    "Learn how to build applications with Spring Boot",
                    "/images/spring-boot.png",
                    "PLA3GkZPtsafacdBLdd3p1DyRd5FGfr3Ue",
                    "https://spring.io/guides/gs/spring-boot"
            ),
            new Course(
                    "JavaScript for Beginners",
                    "Master the fundamentals of JavaScript programming",
                    "/images/javascript.png",
                    "PLGjplNEQ1it_oTvuLRNqXfz_v_0pq6unW",
                    "https://javascript.info/"
            ),
            new Course(
                    "Python Programming",
                    "Learn Python from scratch with practical examples",
                    "/images/python.png",
                    "PLu0W_9lII9agwh1XjRt242xIpHhPT2llg",
                    "https://docs.python.org/3/tutorial/"
            ),
            new Course(
                    "React JS Crash Course",
                    "Build modern web applications with React",
                    "/images/react.png",
                    "PLwGdqUZWnOp1Rab71vx2zMF6qpwGDB2Z1",
                    "https://reactjs.org/docs/getting-started.html"
            ),
            new Course(
                    "DevOps Essentials",
                    "Introduction to DevOps practices and tools",
                    "/images/devops.png",
                    "PLgIMQe2PKPSL_j7HzkLwKz4HRZvOW2i6s",
                    "https://learn.microsoft.com/en-us/devops/"
            )
    );

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @PostConstruct
    public void init() {
        // Load sample courses if database is empty
        if (courseRepository.count() == 0) {
            courseRepository.saveAll(sampleCourses);
        }
    }

    @GetMapping("/")
    public String showCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "courses";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            return "redirect:/add";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        return "add";
    }

    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Course title is required");
            return "redirect:/add";
        }

        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("success", "Course added successfully!");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}