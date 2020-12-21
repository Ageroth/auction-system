import allroles from '../utils/allroles';
import HomePage from '../pages/HomePage'
import LoginPage from '../pages/LoginPage';
import SignupPage from '../pages/SignupPage'
import ActivationPage from '../pages/ActivationPage';
import PasswordResetPage from '../pages/PasswordResetPage'
import UserTablePage from '../pages/UserTablePage'
import UserDetailsPage from '../pages/UserDetailsPage'
import NotFoundPage from '../pages/NotFoundPage'
import UserPasswordChangePage from '../pages/UserPasswordChangePage'
import UserDetailsEditPage from '../pages/UserDetailsEditPage'
import OwnDetailsPage from '../pages/OwnDetailsPage'
import OwnPasswordChangePage from '../pages/OwnPasswordChangePage'
import OwnDetailsEditPage from '../pages/OwnDetailsEditPage'
import UserAddPage from '../pages/UserAddPage'
import AuctionTablePage from '../pages/AuctionTablePage'
import OwnAuctionTablePage from '../pages/OwnAuctionTablePage'
import AuctionAddPage from '../pages/AuctionAddPage'
import AuctionDetailsPage from '../pages/AuctionDetailsPage'
import AuctionEditPage from '../pages/AuctionEditPage'

const {ADMINISTRATOR, MANAGER, CLIENT} = allroles;

export const routes = [
    {path: '/', component: HomePage, public: true},
    {path: '/login', component: LoginPage, public: true},
    {path: '/signup', component: SignupPage, public: true},
    {path: '/activation/:activationCode', component: ActivationPage, public: true},
    {path: '/password_reset', component: PasswordResetPage, public: true},
    {path: '/password_reset/:passwordResetCode', component: PasswordResetPage, public: true},
    {path: '/users', component: UserTablePage, canAccess: [ADMINISTRATOR]},
    {path: '/users/add', component: UserAddPage, canAccess: [ADMINISTRATOR]},
    {path: '/users/:userId', component: UserDetailsPage, canAccess: [ADMINISTRATOR]},
    {path: '/users/:userId/password_change', component: UserPasswordChangePage, canAccess: [ADMINISTRATOR]},
    {path: '/users/:userId/edit', component: UserDetailsEditPage, canAccess: [ADMINISTRATOR]},
    {path: '/my_profile', component: OwnDetailsPage},
    {path: '/my_profile/password_change', component: OwnPasswordChangePage},
    {path: '/my_profile/edit', component: OwnDetailsEditPage},
    {path: '/auctions', component: AuctionTablePage, canAccess: [MANAGER, CLIENT]},
    {path: '/auctions/my_auctions', component: OwnAuctionTablePage, canAccess: [MANAGER]},
    {path: '/auctions/add', component: AuctionAddPage, canAccess: [MANAGER]},
    {path: '/auctions/:auctionId', component: AuctionDetailsPage, canAccess: [MANAGER, CLIENT]},
    {path: '/auctions/:auctionId/edit', component: AuctionEditPage, canAccess: [MANAGER]},
    {path: '*', component: NotFoundPage, public: true},
];