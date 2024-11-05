from django import forms
from .models import User, Teacher, Department


class UserForm(forms.ModelForm):
    class Meta:
        model = User
        fields = ['name', 'email', 'login', 'password', 'surname', 'middle_name', 'birthday', 'sex', 'role']


class TeacherForm(forms.ModelForm):
    class Meta:
        model = Teacher
        fields = ['user', 'department', 'year_of_start_of_work']


class DepartmentForm(forms.ModelForm):
    class Meta:
        model = Department
        fields = ['title', 'head_of_department']