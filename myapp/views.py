from django.db.models import Q
from django.shortcuts import render
from rest_framework import viewsets, generics, status
from rest_framework.response import Response
from datetime import date

from myapp.models import User, Teacher, Department, UserToGroup, Group, DirectionOfStudy, TeacherStatistics
from myapp.serializers import UserSerializer, TeacherSerializer, DepartmentSerializer, UserToGroupSerializer, \
    GroupSerializer, DirectionOfStudySerializer, TeacherStatisticsSerializer
from django_filters.rest_framework import DjangoFilterBackend
from rest_framework.filters import OrderingFilter, SearchFilter


def home(request):
    return render(request, 'home.html')


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    filter_backends = [DjangoFilterBackend, OrderingFilter, SearchFilter]
    filterset_fields = ['name', 'surname', 'middle_name', 'birthday', 'email',
                        'login', 'sex']
    search_fields = ['name', 'surname', 'middle_name', 'email', 'login', 'sex']
    ordering_fields = ['id', 'name', 'surname', 'birthday']

    def get_queryset(self):
        queryset = self.queryset
        name = self.request.query_params.get('name')
        surname = self.request.query_params.get('surname')
        order_by = self.request.query_params.get('orderBy')
        if name:
            queryset = queryset.filter(Q(user__name__icontains=name))
        if surname:
            queryset = queryset.filter(Q(user__surname__icontains=surname))
        if order_by:
            valid_order_fields = ['id', 'name', 'surname', 'birthday']
            if order_by in valid_order_fields:
                queryset = queryset.order_by(order_by)
            else:
                raise ValueError(f"Invalid order_by field: {order_by}")
        return queryset


class TeacherViewSet(viewsets.ModelViewSet):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer

    def get_queryset(self):
        queryset = self.queryset
        name = self.request.query_params.get('name')
        surname = self.request.query_params.get('surname')
        middle_name = self.request.query_params.get('middle_name')
        birthday = self.request.query_params.get('birthday')
        department = self.request.query_params.get('department')
        order_by = self.request.query_params.get('orderBy')

        if name:
            queryset = queryset.filter(Q(user__name__icontains=name))
        if surname:
            queryset = queryset.filter(Q(user__surname__icontains=surname))
        if middle_name:
            queryset = queryset.filter(Q(user__middle_name__icontains=middle_name))
        if birthday:
            queryset = queryset.filter(Q(user__birthday__icontains=birthday))
        if department:
            queryset = queryset.filter(Q(department_id=department))
        if order_by:

            valid_order_fields = ['id', 'user__name', 'user__surname', 'user__middle_name', 'user__birthday']
            if order_by in valid_order_fields:
                queryset = queryset.order_by(order_by)
            else:
                raise ValueError(f"Invalid order_by field: {order_by}")

        return queryset


class TeacherSearchView(generics.ListAPIView):
    serializer_class = TeacherSerializer

    def get_queryset(self):
        query = self.request.query_params.get('query', '')
        if query:
            return Teacher.objects.filter(
                Q(user__name__icontains=query) |
                Q(user__surname__icontains=query) |
                Q(user__middle_name__icontains=query) |
                Q(user__birthday__icontains=query)
            )
        return Teacher.objects.none()


class UserSearchView(generics.ListAPIView):
    serializer_class = UserSerializer

    def get_queryset(self):
        query = self.request.query_params.get('query', '')
        if query:
            return User.objects.filter(
                Q(name__icontains=query) |
                Q(surname__icontains=query) |
                Q(middle_name__icontains=query) |
                Q(birthday__icontains=query)
            )
        return User.objects.none()


class TeacherFilteredViewSet(viewsets.ModelViewSet):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer
    filter_backends = [DjangoFilterBackend, OrderingFilter, SearchFilter]
    filterset_fields = ['user__name', 'user__surname', 'user__middle_name', 'user__birthday', 'user__email',
                        'user__login', 'user__sex', 'department', 'year_of_start_of_work']
    search_fields = ['user__name', 'user__surname', 'user__middle_name', 'user__email', 'user__login', 'user__sex']
    ordering_fields = ['id', 'user__name', 'user__surname', 'user__birthday', 'year_of_start_of_work']

    def get_queryset(self):
        queryset = self.queryset
        name = self.request.query_params.get('name')
        surname = self.request.query_params.get('surname')
        order_by = self.request.query_params.get('orderBy')
        if name:
            queryset = queryset.filter(Q(user__name__icontains=name))
        if surname:
            queryset = queryset.filter(Q(user__surname__icontains=surname))
        if order_by:
            valid_order_fields = ['id', 'user__name', 'user__surname', 'user__birthday']
            if order_by in valid_order_fields:
                queryset = queryset.order_by(order_by)
            else:
                raise ValueError(f"Invalid order_by field: {order_by}")
        return queryset


class TeacherFilteredView(generics.ListAPIView):
    serializer_class = TeacherSerializer

    def get_queryset(self):
        queryset = Teacher.objects.all()

        name = self.request.query_params.get('name')
        surname = self.request.query_params.get('surname')
        order_by = self.request.query_params.get('orderBy')
        if name:
            queryset = queryset.filter(Q(user__name__icontains=name))
        if surname:
            queryset = queryset.filter(Q(user__surname__icontains=surname))
        if order_by:
            valid_order_fields = ['id', 'user__name', 'user__surname', 'user__birthday']
            if order_by in valid_order_fields:
                queryset = queryset.order_by(order_by)
            else:
                raise ValueError(f"Invalid order_by field: {order_by}")

        min_age = self.request.query_params.get('min_age', None)
        max_age = self.request.query_params.get('max_age', None)
        min_year_of_start = self.request.query_params.get('min_year_of_start', None)
        max_year_of_start = self.request.query_params.get('max_year_of_start', None)
        gender = self.request.query_params.get('sex', None)

        if min_age or max_age:
            today = date.today()
            if min_age:
                min_date = today.replace(year=today.year - int(min_age))
                queryset = queryset.filter(user__birthday__lte=min_date)
            if max_age:
                max_date = today.replace(year=today.year - int(max_age))
                queryset = queryset.filter(user__birthday__gte=max_date)

        # Фильтрация по году начала работы
        if min_year_of_start:
            queryset = queryset.filter(year_of_start_of_work__gte=min_year_of_start)
        if max_year_of_start:
            queryset = queryset.filter(year_of_start_of_work__lte=max_year_of_start)

        # Фильтрация по полу (gender)
        if gender:
            if gender not in ['Male', 'Female']:
                return Response({"error": "Invalid gender. Choose 'male' or 'female'."}, status=status.HTTP_400_BAD_REQUEST)
            queryset = queryset.filter(user__sex=gender)

        return queryset


class StatisticsView(viewsets.ReadOnlyModelViewSet):
    queryset = TeacherStatistics.objects.all()
    serializer_class = TeacherStatisticsSerializer


class DepartmentViewSet(viewsets.ModelViewSet):
    queryset = Department.objects.all()
    serializer_class = DepartmentSerializer


class UserDetailView(generics.RetrieveUpdateDestroyAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer


class DepartmentDetailView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Department.objects.all()
    serializer_class = DepartmentSerializer


class TeacherDetailView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer


class DirectionOfStudyViewSet(viewsets.ModelViewSet):
    queryset = DirectionOfStudy.objects.all()
    serializer_class = DirectionOfStudySerializer


class GroupViewSet(viewsets.ModelViewSet):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer


class UserToGroupViewSet(viewsets.ModelViewSet):
    queryset = UserToGroup.objects.all()
    serializer_class = UserToGroupSerializer

    def get_queryset(self):
        queryset = self.queryset
        name = self.request.query_params.get('name')
        surname = self.request.query_params.get('surname')
        middle_name = self.request.query_params.get('middle_name')
        birthday = self.request.query_params.get('birthday')
        department = self.request.query_params.get('department')
        order_by = self.request.query_params.get('orderBy')

        if name:
            queryset = queryset.filter(Q(user__name__icontains=name))
        if surname:
            queryset = queryset.filter(Q(user__surname__icontains=surname))
        if middle_name:
            queryset = queryset.filter(Q(user__middle_name__icontains=middle_name))
        if birthday:
            queryset = queryset.filter(Q(user__birthday__icontains=birthday))
        if department:
            queryset = queryset.filter(Q(department_id=department))
        if order_by:

            valid_order_fields = ['id', 'user__name', 'user__surname', 'user__middle_name', 'user__birthday']
            if order_by in valid_order_fields:
                queryset = queryset.order_by(order_by)
            else:
                raise ValueError(f"Invalid order_by field: {order_by}")

        return queryset


class DirectionOfStudyView(generics.RetrieveUpdateDestroyAPIView):
    queryset = DirectionOfStudy.objects.all()
    serializer_class = DirectionOfStudySerializer


class GroupView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer


class UserToGroupView(generics.RetrieveUpdateDestroyAPIView):
    queryset = UserToGroup.objects.all()
    serializer_class = UserToGroupSerializer
