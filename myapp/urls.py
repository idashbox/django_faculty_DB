from django.urls import path, include
from rest_framework.routers import DefaultRouter

from .login_logout import LoginView, LogoutView, ProfileView
from .views import UserViewSet, TeacherViewSet, DepartmentViewSet, UserDetailView, DepartmentDetailView, \
    TeacherDetailView, DirectionOfStudyViewSet, GroupViewSet, UserToGroupViewSet, DirectionOfStudyView, GroupView, \
    UserToGroupView, StatisticsView, TeacherSearchView, TeacherFilteredView, UserSearchView

router = DefaultRouter()
router.register(r'users', UserViewSet)
router.register(r'teachers', TeacherViewSet, basename='teacher')
router.register(r'departments', DepartmentViewSet)
router.register(r'directions', DirectionOfStudyViewSet)
router.register(r'groups', GroupViewSet)
router.register(r'user_to_groups', UserToGroupViewSet)


urlpatterns = [
    path('api/', include(router.urls)),
    path('login/', LoginView.as_view(), name='login'),
    path('logout/', LogoutView.as_view(), name='logout'),
    path('profile/', ProfileView.as_view(), name='profile'),
    path('api/users/<int:pk>/', UserDetailView.as_view(), name='user-detail'),
    path('users/search/', UserSearchView.as_view(), name='user-search'),
    path('api/departments/<int:pk>/', DepartmentDetailView.as_view(), name='department-detail'),
    path('api/teachers/<int:pk>/', TeacherDetailView.as_view(), name='teacher-detail'),
    path('api/teachers/', TeacherViewSet.as_view({'get': 'list'})),
    path('teachers/search/', TeacherSearchView.as_view(), name='teacher-search'),
    path('api/directions/<int:pk>/', DirectionOfStudyView.as_view(), name='direction-detail'),
    path('api/groups/<int:pk>/', GroupView.as_view(), name='group-detail'),
    path('api/user_to_groups/<int:pk>/', UserToGroupView.as_view(), name='user-to-group-detail'),
    path('api/statistics/', StatisticsView.as_view({'get': 'list'}), name='statistics'),
    path('teachers/filtered/', TeacherFilteredView.as_view(), name='teacher-filtered-api')
]
