package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import edu.ccrm.interfaces.Searchable;

import java.util.*;
import java.util.stream.Collectors;


public class CourseService implements Searchable<Course> {
    private final List<Course> courses;
    
    public CourseService() {
        this.courses = new ArrayList<>();
    }
    
    public void addCourse(Course course) {
        courses.add(course);
        System.out.println("Added course: " + course.getCode() + " - " + course.getTitle());
    }
    
   
    @Override
    public List<Course> search(String keyword) {
        return courses.stream()
                     .filter(course -> 
                         course.getCode().toLowerCase().contains(keyword.toLowerCase()) ||
                         course.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                         course.getInstructor().toLowerCase().contains(keyword.toLowerCase()) ||
                         course.getDepartment().toLowerCase().contains(keyword.toLowerCase()))
                     .collect(Collectors.toList());
    }
    
   
    public List<Course> searchByInstructor(String instructor) {
        return courses.stream()
                     .filter(course -> course.getInstructor().equalsIgnoreCase(instructor))
                     .collect(Collectors.toList());
    }
    
  
    public List<Course> searchByDepartment(String department) {
        return courses.stream()
                     .filter(course -> course.getDepartment().equalsIgnoreCase(department))
                     .collect(Collectors.toList());
    }
    
   
    public List<Course> searchBySemester(Semester semester) {
        return courses.stream()
                     .filter(course -> course.getSemester() == semester)
                     .collect(Collectors.toList());
    }
    
    
    public List<Course> getActiveCourses() {
        return courses.stream()
                     .filter(Course::canEnroll)
                     .collect(Collectors.toList());
    }
    
    
    public Optional<Course> getCourseByCode(String code) {
        return courses.stream()
                     .filter(course -> course.getCode().equalsIgnoreCase(code))
                     .findFirst();
    }
    
    
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }
}