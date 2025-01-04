from django_filters import rest_framework as filters
from django.db.models import Q
from datetime import datetime, timedelta
from .models import Teacher

class TeacherFilter(filters.FilterSet):
    name = filters.CharFilter(field_name='user__first_name', lookup_expr='icontains')
    surname = filters.CharFilter(field_name='user__last_name', lookup_expr='icontains')
    year_of_start_of_work_min = filters.NumberFilter(field_name='year_of_start_of_work', lookup_expr='gte')
    year_of_start_of_work_max = filters.NumberFilter(field_name='year_of_start_of_work', lookup_expr='lte')
    age_min = filters.NumberFilter(method='filter_age_min')
    age_max = filters.NumberFilter(method='filter_age_max')
    sex = filters.ChoiceFilter(choices=[('male', 'Male'), ('female', 'Female')], method='filter_sex')

    class Meta:
        model = Teacher
        fields = ['name', 'surname', 'year_of_start_of_work_min', 'year_of_start_of_work_max', 'age_min', 'age_max', 'sex']

    def filter_age_min(self, queryset, name, value):
        current_date = datetime.now().date()
        min_birth_date = current_date - timedelta(days=value*365)
        return queryset.filter(user__date_of_birth__lte=min_birth_date)

    def filter_age_max(self, queryset, name, value):
        current_date = datetime.now().date()
        max_birth_date = current_date - timedelta(days=value*365)
        return queryset.filter(user__date_of_birth__gte=max_birth_date)

    def filter_sex(self, queryset, name, value):
        if value == 'male':
            return queryset.filter(user__profile__gender='male')
        elif value == 'female':
            return queryset.filter(user__profile__gender='female')
        return queryset
