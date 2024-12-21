from django import forms
from myapp.models import User, Teacher, Department, Group, UserToGroup, DirectionOfStudy


class UserForm(forms.ModelForm):
    class Meta:
        model = User
        fields = ['name', 'email', 'login', 'password', 'surname', 'middle_name', 'birthday', 'sex', 'role']


class TeacherForm(forms.ModelForm):
    class Meta:
        ordering = ['id']
        model = Teacher
        fields = ['user', 'department', 'year_of_start_of_work']


class DepartmentForm(forms.ModelForm):
    class Meta:
        model = Department
        fields = ['title', 'head_of_department']


class DirectionOfStudyForm(forms.ModelForm):
    class Meta:
        model = DirectionOfStudy
        fields = ['title', 'department', 'code', 'degree']


class GroupForm(forms.ModelForm):
    class Meta:
        model = Group
        fields = ['direction', 'headmen', 'curator', 'course', 'number']


class UserToGroupForm(forms.ModelForm):
    class Meta:
        model = UserToGroup
        fields = ['user', 'group']