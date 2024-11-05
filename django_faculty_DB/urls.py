from django.contrib import admin
from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name='home'),
    path('users/', views.UserListView.as_view(), name='user_list'),
    path('users/new/', views.UserCreateView.as_view(), name='user_new'),
    path('users/<int:pk>/edit/', views.UserUpdateView.as_view(), name='user_edit'),
    path('users/<int:pk>/delete/', views.UserDeleteView.as_view(), name='user_delete'),
    path('teachers/', views.TeacherListView.as_view(), name='teacher_list'),
    path('teachers/new/', views.TeacherCreateView.as_view(), name='teacher_new'),
    path('teachers/<int:pk>/edit/', views.TeacherUpdateView.as_view(), name='teacher_edit'),
    path('teachers/<int:pk>/delete/', views.TeacherDeleteView.as_view(), name='teacher_delete'),
    path('departments/', views.DepartmentListView.as_view(), name='department_list'),
    path('departments/new/', views.DepartmentCreateView.as_view(), name='department_new'),
    path('departments/<int:pk>/edit/', views.DepartmentUpdateView.as_view(), name='department_edit'),
    path('departments/<int:pk>/delete/', views.DepartmentDeleteView.as_view(), name='department_delete'),
]
