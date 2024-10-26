from django.contrib import admin
from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name='home'),
    path('admin/', admin.site.urls),
    path('users/', views.user_list, name='user_list'),
    path('teachers/', views.teacher_list, name='teacher_list'),
    path('departments/', views.department_list, name='department_list'),
]
