from django.core.paginator import Paginator
from django.shortcuts import render
from .models import User, Teacher, Department


def home(request):
    return render(request, 'home.html')


def user_list(request):
    users = User.objects.all()
    paginator = Paginator(users, 100)
    page_number = request.GET.get('page')
    page_obj = paginator.get_page(page_number)
    return render(request, 'user_list.html', {'page_obj': page_obj})


def teacher_list(request):
    teachers = Teacher.objects.all()
    paginator = Paginator(teachers, 100)
    page_number = request.GET.get('page')
    page_obj = paginator.get_page(page_number)
    return render(request, 'teacher_list.html', {'page_obj': page_obj})


def department_list(request):
    departments = Department.objects.all()
    paginator = Paginator(departments, 100)
    page_number = request.GET.get('page')
    page_obj = paginator.get_page(page_number)
    return render(request, 'department_list.html', {'page_obj': page_obj})