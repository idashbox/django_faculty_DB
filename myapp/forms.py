from django import forms
from myapp.models import User, Teacher, Department, Group, UserToGroup, DirectionOfStudy
from datetime import date


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


class TeacherFilterForm(forms.Form):
    SEX_CHOICES = [
        ('male', 'Мужчина'),
        ('female', 'Женщина'),
    ]

    min_year_of_start_of_work = forms.IntegerField(required=False, label='Год начала работы (от)')
    max_year_of_start_of_work = forms.IntegerField(required=False, label='Год начала работы (до)')
    min_age = forms.IntegerField(required=False, label='Возраст (от)')
    max_age = forms.IntegerField(required=False, label='Возраст (до)')
    sex = forms.ChoiceField(choices=SEX_CHOICES, required=False, label='Пол')

    def filter_teachers(self):
        queryset = Teacher.objects.all()

        if self.cleaned_data.get('min_year_of_start_of_work'):
            queryset = queryset.filter(year_of_start_of_work__gte=self.cleaned_data['min_year_of_start_of_work'])

        if self.cleaned_data.get('max_year_of_start_of_work'):
            queryset = queryset.filter(year_of_start_of_work__lte=self.cleaned_data['max_year_of_start_of_work'])

        if self.cleaned_data.get('sex'):
            queryset = queryset.filter(user__sex=self.cleaned_data['sex'])

        if self.cleaned_data.get('min_age') or self.cleaned_data.get('max_age'):
            today = date.today()
            min_birthday = date(today.year - self.cleaned_data.get('max_age', 100), today.month, today.day)
            max_birthday = date(today.year - self.cleaned_data.get('min_age', 0), today.month, today.day)
            queryset = queryset.filter(user__birthday__range=(min_birthday, max_birthday))

        return queryset