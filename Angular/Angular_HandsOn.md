# Angular (v20.0) Hands-On Exercises Solutions

This document provides the solutions and code snippets for the 10 Hands-On exercises for the **Student Course Portal** application.

## Hands-On 1: Environment Setup, Project Structure & First Component

**Task 1: Scaffold and Explore the Angular Project**
```bash
ng new student-course-portal --routing --style=css
cd student-course-portal
ng serve
```

*Notes on files (`notes.txt`):*
* `angular.json`: Workspace configuration and build settings.
* `tsconfig.json`: Base TypeScript configuration for the workspace.
* `tsconfig.app.json`: TypeScript configuration specifically for the app.
* `package.json`: NPM dependencies and project scripts.
* `src/main.ts`: Application entry point to bootstrap the root component.
* `src/app/app.config.ts`: Configuration for standalone Angular (providers, routing).
* `src/app/app.component.ts`: The root component of the application.
* `src/index.html`: The main HTML file served in the browser.

*Budgets (`angular.json`):* `maximumWarning` and `maximumError` control bundle size limits to warn or fail the build if the app becomes too large, ensuring performance.

**Task 2: Create and Organise Components**
```bash
ng generate component components/header
ng generate component pages/home
ng generate component pages/course-list
ng generate component pages/student-profile
```

**`header.component.html`**
```html
<nav>
  <h2>Student Course Portal</h2>
  <ul>
    <li><a routerLink="/">Home</a></li>
    <li><a routerLink="/courses">Courses</a></li>
    <li><a routerLink="/profile">Profile</a></li>
  </ul>
</nav>
```

**`home.component.html`**
```html
<h1>Welcome to the Student Course Portal</h1>
<p>Manage your courses and enrollments easily.</p>
<div>
  <span>Courses Available: 12</span> |
  <span>Enrolled: 3</span> |
  <span>GPA: 3.8</span>
</div>
```

**`app.component.html`**
```html
<app-header></app-header>
<router-outlet></router-outlet>
```

---

## Hands-On 2: Data Binding, Lifecycle Hooks & Component Communication

**Task 1: All Four Binding Types (`home.component.ts` & `html`)**
```typescript
import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  portalName = 'Student Course Portal';
  isPortalActive = true;
  message = '';
  searchTerm = '';

  ngOnInit() {
    console.log('HomeComponent initialised — courses loaded');
  }

  ngOnDestroy() {
    console.log('HomeComponent destroyed');
  }

  onEnrollClick() {
    this.message = 'Enrollment opened!';
  }
}
```

```html
<h1>{{ portalName }}</h1>
<button [disabled]="!isPortalActive" (click)="onEnrollClick()">Enroll Now</button>
<p>{{ message }}</p>

<!-- Two-way binding requires FormsModule -->
<input [(ngModel)]="searchTerm" placeholder="Search..." />
<p>Searching for: {{ searchTerm }}</p>

<!-- 
Difference: 
[property] is one-way from component to DOM.
[(ngModel)] is two-way, updating the component when the DOM changes and vice versa. 
-->
```

**Task 2 & 3: Lifecycle Hooks and @Input / @Output (`course-card.component.ts`)**
```typescript
import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-course-card',
  template: `
    <div class="card">
      <h3>{{ course.name }} ({{ course.code }})</h3>
      <p>Credits: {{ course.credits }}</p>
      <button (click)="enrollRequested.emit(course.id)">Enroll</button>
    </div>
  `
})
export class CourseCardComponent implements OnChanges {
  @Input() course!: { id: number, name: string, code: string, credits: number };
  @Output() enrollRequested = new EventEmitter<number>();

  ngOnChanges(changes: SimpleChanges) {
    console.log('Course changed:', changes['course'].previousValue, '->', changes['course'].currentValue);
  }
}
```

**`course-list.component.ts`**
```typescript
export class CourseListComponent {
  courses = [
    { id: 1, name: 'Angular Basics', code: 'CS101', credits: 3 },
    { id: 2, name: 'Advanced Java', code: 'CS201', credits: 4 }
  ];
  selectedCourseId?: number;

  onEnroll(courseId: number) {
    console.log('Enrolling in course:', courseId);
    this.selectedCourseId = courseId;
  }
}
```
```html
<app-course-card 
  *ngFor="let c of courses" 
  [course]="c" 
  (enrollRequested)="onEnroll($event)">
</app-course-card>
<p *ngIf="selectedCourseId">Selected course ID: {{ selectedCourseId }}</p>
```

---

## Hands-On 3: Directives & Pipes — Built-in and Custom

**Task 1: Structural Directives (`course-list.component.html`)**
```html
<p *ngIf="isLoading; else courseList">Loading courses...</p>

<ng-template #courseList>
  <div *ngIf="courses.length > 0; else noCourses">
    <app-course-card 
      *ngFor="let course of courses; trackBy: trackByCourseId" 
      [course]="course"
      (enrollRequested)="onEnroll($event)">
    </app-course-card>
  </div>
</ng-template>

<ng-template #noCourses><p>No courses available.</p></ng-template>
```
*Note: `trackBy` prevents Angular from re-rendering the whole list when the array changes, improving performance.*

**Task 2: Attribute Directives (`course-card.component.html` & `ts`)**
```typescript
get cardClasses() {
  return {
    'card--enrolled': this.isEnrolled,
    'card--full': this.course.credits >= 4,
    'expanded': this.isExpanded
  };
}
get borderStyle() {
  switch(this.course.gradeStatus) {
    case 'passed': return 'green';
    case 'failed': return 'red';
    default: return 'grey';
  }
}
```
```html
<div [ngClass]="cardClasses" [ngStyle]="{'border-left-color': borderStyle}">
  <!-- Switch Example -->
  <span [ngSwitch]="course.gradeStatus">
    <span *ngSwitchCase="'passed'" class="badge green">Passed</span>
    <span *ngSwitchCase="'failed'" class="badge red">Failed</span>
    <span *ngSwitchDefault class="badge grey">Pending</span>
  </span>
</div>
```

**Task 3: Custom Directive and Pipe**
```bash
ng generate directive directives/highlight
ng generate pipe pipes/credit-label
```

**`highlight.directive.ts`**
```typescript
import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({ selector: '[appHighlight]' })
export class HighlightDirective {
  @Input() appHighlight = 'yellow';

  constructor(private el: ElementRef) {}

  @HostListener('mouseenter') onMouseEnter() {
    this.el.nativeElement.style.backgroundColor = this.appHighlight;
  }
  @HostListener('mouseleave') onMouseLeave() {
    this.el.nativeElement.style.backgroundColor = '';
  }
}
```

**`credit-label.pipe.ts`**
```typescript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'creditLabel' })
export class CreditLabelPipe implements PipeTransform {
  transform(value: number | null | undefined): string {
    if (!value || value === 0) return 'No Credits';
    return value === 1 ? '1 Credit' : `${value} Credits`;
  }
}
```
Usage: `{{ course.credits | creditLabel }}`

---

## Hands-On 4: Template-Driven Forms & Validation

**`enrollment-form.component.html`**
```html
<form #enrollForm="ngForm" (ngSubmit)="onSubmit(enrollForm)">
  <div>
    <label>Name:</label>
    <input type="text" name="studentName" [(ngModel)]="studentName" required minlength="3" #nameCtrl="ngModel">
    <span *ngIf="nameCtrl.touched && nameCtrl.errors?.['required']" class="error">Name is required.</span>
    <span *ngIf="nameCtrl.touched && nameCtrl.errors?.['minlength']" class="error">Name must be at least 3 characters.</span>
  </div>
  
  <div>
    <label>Email:</label>
    <input type="email" name="studentEmail" [(ngModel)]="studentEmail" required email #emailCtrl="ngModel">
  </div>
  
  <div>
    <label>Course ID:</label>
    <input type="number" name="courseId" [(ngModel)]="courseId" required>
  </div>
  
  <div>
    <label>Semester:</label>
    <select name="preferredSemester" [(ngModel)]="preferredSemester">
      <option value="Odd">Odd</option>
      <option value="Even">Even</option>
    </select>
  </div>
  
  <div>
    <label>
      <input type="checkbox" name="agreeToTerms" [(ngModel)]="agreeToTerms" required> Agree to terms
    </label>
  </div>
  
  <button type="submit" [disabled]="enrollForm.invalid">Submit</button>
  <button type="button" (click)="enrollForm.resetForm()">Reset</button>
</form>

<div *ngIf="submitted">Enrollment request submitted successfully!</div>
```
```css
.ng-invalid.ng-touched { border-color: red; }
.ng-valid.ng-touched { border-color: green; }
```

---

## Hands-On 5: Reactive Forms

**`reactive-enrollment-form.component.ts`**
```typescript
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, AbstractControl, ValidationErrors } from '@angular/forms';

@Component({
  selector: 'app-reactive-enrollment-form',
  templateUrl: './reactive-enrollment-form.component.html'
})
export class ReactiveEnrollmentFormComponent implements OnInit {
  enrollForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.enrollForm = this.fb.group({
      studentName: ['', [Validators.required, Validators.minLength(3)]],
      studentEmail: ['', [Validators.required, Validators.email], [this.simulateEmailCheck]],
      courseId: [null, [Validators.required, this.noCourseCode]],
      preferredSemester: ['Odd', Validators.required],
      agreeToTerms: [false, Validators.requiredTrue],
      additionalCourses: this.fb.array([])
    });
  }

  get additionalCourses() {
    return this.enrollForm.get('additionalCourses') as FormArray;
  }

  addCourse() {
    this.additionalCourses.push(this.fb.control('', Validators.required));
  }

  removeCourse(index: number) {
    this.additionalCourses.removeAt(index);
  }

  onSubmit() {
    console.log(this.enrollForm.value);
    console.log(this.enrollForm.getRawValue());
  }

  // Custom Sync Validator
  noCourseCode(control: AbstractControl): ValidationErrors | null {
    if (control.value && typeof control.value === 'string' && control.value.startsWith('XX')) {
      return { noCourseCode: true };
    }
    return null;
  }

  // Custom Async Validator
  simulateEmailCheck(control: AbstractControl): Promise<ValidationErrors | null> {
    return new Promise(resolve => {
      setTimeout(() => {
        if (control.value?.includes('test@')) resolve({ emailTaken: true });
        else resolve(null);
      }, 800);
    });
  }
}
```

---

## Hands-On 6: Services & Dependency Injection

**`course.service.ts`**
```typescript
import { Injectable } from '@angular/core';
import { Course } from '../models/course.model';

@Injectable({ providedIn: 'root' })
export class CourseService {
  private courses: Course[] = [
    { id: 1, name: 'Angular', code: 'CS101', credits: 3, gradeStatus: 'pending' },
    // ...
  ];

  getCourses(): Course[] { return this.courses; }
  getCourseById(id: number): Course | undefined { return this.courses.find(c => c.id === id); }
  addCourse(course: Course): void { this.courses.push(course); }
}
```

**`enrollment.service.ts`**
```typescript
import { Injectable } from '@angular/core';
import { CourseService } from './course.service';
import { Course } from '../models/course.model';

@Injectable({ providedIn: 'root' })
export class EnrollmentService {
  private enrolledCourseIds: number[] = [];

  constructor(private courseService: CourseService) {}

  enroll(courseId: number): void {
    if (!this.isEnrolled(courseId)) this.enrolledCourseIds.push(courseId);
  }
  unenroll(courseId: number): void {
    this.enrolledCourseIds = this.enrolledCourseIds.filter(id => id !== courseId);
  }
  isEnrolled(courseId: number): boolean {
    return this.enrolledCourseIds.includes(courseId);
  }
  getEnrolledCourses(): Course[] {
    return this.courseService.getCourses().filter(c => this.isEnrolled(c.id));
  }
}
```
*Note on component-level DI: Providing `NotificationService` in `@Component({ providers: [NotificationService] })` creates a new instance specific to that component and its children, preventing state leakage across multiple instances.*

---

## Hands-On 7: Angular Routing

**`app.routes.ts`**
```typescript
import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'courses', component: CoursesLayoutComponent, children: [
      { path: '', component: CourseListComponent },
      { path: ':id', component: CourseDetailComponent }
  ]},
  { path: 'profile', canActivate: [AuthGuard], component: StudentProfileComponent },
  { path: 'enroll', loadChildren: () => import('./features/enrollment/enrollment.module').then(m => m.EnrollmentModule) },
  { path: '**', component: NotFoundComponent }
];
```

**`auth.guard.ts`**
```typescript
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  isLoggedIn = true; // Hardcoded for demo

  constructor(private router: Router) {}

  canActivate(): boolean {
    if (this.isLoggedIn) return true;
    this.router.navigate(['/']);
    return false;
  }
}
```

**`unsaved-changes.guard.ts`**
```typescript
import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';
import { ReactiveEnrollmentFormComponent } from '../pages/reactive-enrollment-form.component';

@Injectable({ providedIn: 'root' })
export class UnsavedChangesGuard implements CanDeactivate<ReactiveEnrollmentFormComponent> {
  canDeactivate(component: ReactiveEnrollmentFormComponent): boolean {
    if (component.enrollForm.dirty) {
      return window.confirm('You have unsaved changes. Leave?');
    }
    return true;
  }
}
```

---

## Hands-On 8: HTTP Client & Interceptors

**`course.service.ts` (Refactored for HTTP)**
```typescript
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError, tap, retry } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class CourseService {
  private apiUrl = 'http://localhost:3000/courses';

  constructor(private http: HttpClient) {}

  getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.apiUrl).pipe(
      retry(2),
      map(courses => courses.filter(c => c.credits > 0)),
      tap(courses => console.log('Courses loaded:', courses.length)),
      catchError(err => {
        console.error(err);
        return throwError(() => new Error('Failed to load courses. Please try again.'));
      })
    );
  }

  createCourse(course: Omit<Course, 'id'>): Observable<Course> {
    return this.http.post<Course>(this.apiUrl, course);
  }
}
```

**`auth.interceptor.ts`**
```typescript
import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const clonedReq = req.clone({
    setHeaders: { Authorization: 'Bearer mock-token-12345' }
  });
  return next(clonedReq);
};
```

---

## Hands-On 9: State Management with NgRx

**`course.actions.ts`**
```typescript
import { createAction, props } from '@ngrx/store';
import { Course } from '../../models/course.model';

export const loadCourses = createAction('[Course] Load Courses');
export const loadCoursesSuccess = createAction('[Course] Load Courses Success', props<{ courses: Course[] }>());
export const loadCoursesFailure = createAction('[Course] Load Courses Failure', props<{ error: string }>());
```

**`course.reducer.ts`**
```typescript
import { createReducer, on } from '@ngrx/store';
import * as CourseActions from './course.actions';

export const initialState = { courses: [], loading: false, error: null };

export const courseReducer = createReducer(
  initialState,
  on(CourseActions.loadCourses, state => ({ ...state, loading: true })),
  on(CourseActions.loadCoursesSuccess, (state, { courses }) => ({ ...state, loading: false, courses })),
  on(CourseActions.loadCoursesFailure, (state, { error }) => ({ ...state, loading: false, error }))
);
```

**`course.effects.ts`**
```typescript
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { CourseService } from '../../services/course.service';
import * as CourseActions from './course.actions';
import { switchMap, map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable()
export class CourseEffects {
  loadCourses$ = createEffect(() =>
    this.actions$.pipe(
      ofType(CourseActions.loadCourses),
      switchMap(() => this.courseService.getCourses().pipe(
        map(courses => CourseActions.loadCoursesSuccess({ courses })),
        catchError(error => of(CourseActions.loadCoursesFailure({ error: error.message })))
      ))
    )
  );
  constructor(private actions$: Actions, private courseService: CourseService) {}
}
```

---

## Hands-On 10: Unit Testing

**`course-card.component.spec.ts`**
```typescript
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { CourseCardComponent } from './course-card.component';

describe('CourseCardComponent', () => {
  let component: CourseCardComponent;
  let fixture: ComponentFixture<CourseCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({ declarations: [CourseCardComponent] });
    fixture = TestBed.createComponent(CourseCardComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render course name', () => {
    component.course = { id: 1, name: 'Data Structures', code: 'CS101', credits: 4, gradeStatus: 'passed' };
    fixture.detectChanges();
    const h3 = fixture.debugElement.query(By.css('h3')).nativeElement;
    expect(h3.textContent).toContain('Data Structures');
  });

  it('should emit course id on enroll click', () => {
    component.course = { id: 1, name: 'Data', code: 'CS1', credits: 3, gradeStatus: 'passed' };
    spyOn(component.enrollRequested, 'emit');
    fixture.detectChanges();
    fixture.debugElement.query(By.css('button')).nativeElement.click();
    expect(component.enrollRequested.emit).toHaveBeenCalledWith(1);
  });
});
```

**`course.service.spec.ts`**
```typescript
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CourseService } from './course.service';

describe('CourseService', () => {
  let service: CourseService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CourseService]
    });
    service = TestBed.inject(CourseService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch courses', () => {
    const mockCourses = [{ id: 1, name: 'Angular', code: 'CS1', credits: 3 }];
    service.getCourses().subscribe(courses => {
      expect(courses.length).toBe(1);
      expect(courses).toEqual(mockCourses);
    });
    const req = httpMock.expectOne('http://localhost:3000/courses');
    expect(req.request.method).toBe('GET');
    req.flush(mockCourses);
  });
});
```
