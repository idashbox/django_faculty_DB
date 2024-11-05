from django.shortcuts import render
from django.urls import reverse_lazy
from django.views.generic import ListView, CreateView, UpdateView, DeleteView
from .models import User, Teacher, Department
from .forms import UserForm, TeacherForm, DepartmentForm

def home(request):
    return render(request, 'home.html')

class ObjectListView(ListView):
    paginate_by = 100

class ObjectCreateView(CreateView):
    template_name = 'object_form.html'

class ObjectUpdateView(UpdateView):
    template_name = 'object_form.html'

class ObjectDeleteView(DeleteView):
    template_name = 'object_confirm_delete.html'

class UserListView(ObjectListView):
    model = User
    template_name = 'user_list.html'

class UserCreateView(ObjectCreateView):
    model = User
    form_class = UserForm
    success_url = reverse_lazy('user_list')

class UserUpdateView(ObjectUpdateView):
    model = User
    form_class = UserForm
    success_url = reverse_lazy('user_list')

class UserDeleteView(ObjectDeleteView):
    model = User
    success_url = reverse_lazy('user_list')

class TeacherListView(ObjectListView):
    model = Teacher
    template_name = 'teacher_list.html'

class TeacherCreateView(ObjectCreateView):
    model = Teacher
    form_class = TeacherForm
    success_url = reverse_lazy('teacher_list')

class TeacherUpdateView(ObjectUpdateView):
    model = Teacher
    form_class = TeacherForm
    success_url = reverse_lazy('teacher_list')

class TeacherDeleteView(ObjectDeleteView):
    model = Teacher
    success_url = reverse_lazy('teacher_list')

class DepartmentListView(ObjectListView):
    model = Department
    template_name = 'department_list.html'

class DepartmentCreateView(ObjectCreateView):
    model = Department
    form_class = DepartmentForm
    success_url = reverse_lazy('department_list')

class DepartmentUpdateView(ObjectUpdateView):
    model = Department
    form_class = DepartmentForm
    success_url = reverse_lazy('department_list')

class DepartmentDeleteView(ObjectDeleteView):
    model = Department
    success_url = reverse_lazy('department_list')
