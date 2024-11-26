from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import UserViewSet, TeacherViewSet, DepartmentViewSet, UserDetailView, DepartmentDetailView, TeacherDetailView

router = DefaultRouter()
router.register(r'users', UserViewSet)
router.register(r'teachers', TeacherViewSet)
router.register(r'departments', DepartmentViewSet)

urlpatterns = [
    path('api/', include(router.urls)),
    # Эти пути для получения/редактирования конкретных записей
    path('api/users/<int:pk>/', UserDetailView.as_view(), name='user-detail'),
    path('api/departments/<int:pk>/', DepartmentDetailView.as_view(), name='department-detail'),
    path('api/teachers/<int:pk>/', TeacherDetailView.as_view(), name='teacher-detail'),
]
