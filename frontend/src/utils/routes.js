import allroles from '../utils/allroles';
import HomePage from '../pages/SharedPages/HomePage'
import LoginPage from '../pages/SharedPages/LoginPage';
import SignupPage from '../pages/SharedPages/SignupPage'
import ActivationPage from '../pages/SharedPages/ActivationPage';
import PasswordResetPage from '../pages/SharedPages/PasswordResetPage'
import UserTablePage from '../pages/AdministratorPages/UserTablePage'
import UserDetailsPage from '../pages/AdministratorPages/UserDetailsPage'
import ForbiddenPage from "../pages/SharedPages/ForbiddenPage";
import NotFoundPage from '../pages/SharedPages/NotFoundPage'
import InternalServerErrorPage from "../pages/SharedPages/InternalServerErrorPage";
import UserPasswordChangePage from '../pages/AdministratorPages/UserPasswordChangePage'
import UserDetailsEditPage from '../pages/AdministratorPages/UserDetailsEditPage'
import OwnDetailsPage from '../pages/SharedPages/OwnDetailsPage'
import OwnPasswordChangePage from '../pages/SharedPages/OwnPasswordChangePage'
import OwnDetailsEditPage from '../pages/SharedPages/OwnDetailsEditPage'
import UserAddPage from '../pages/AdministratorPages/UserAddPage'
import AuctionTablePage from '../pages/SharedPages/AuctionTablePage'
import OwnAuctionTablePage from '../pages/ManagerPages/OwnAuctionTablePage'
import AuctionAddPage from '../pages/ManagerPages/AuctionAddPage'
import AuctionDetailsPage from '../pages/SharedPages/AuctionDetailsPage'
import OwnAuctionDetailsPage from '../pages/ManagerPages/OwnAuctionDetailsPage'
import AuctionEditPage from '../pages/ManagerPages/AuctionEditPage'
import BiddingTablePage from '../pages/ClientPages/BiddingTablePage'
import BiddingDetailsPage from '../pages/ClientPages/BiddingDetailsPage'

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
    {path: '/auctions/:auctionId', component: AuctionDetailsPage, canAccess: [MANAGER, CLIENT]},
    {path: '/auctions/:auctionId/edit', component: AuctionEditPage, canAccess: [MANAGER]},
    {path: '/my_auctions', component: OwnAuctionTablePage, canAccess: [MANAGER]},
    {path: '/my_auctions/add', component: AuctionAddPage, canAccess: [MANAGER]},
    {path: '/my_auctions/:auctionId', component: OwnAuctionDetailsPage, canAccess: [MANAGER]},
    {path: '/my_auctions/:auctionId/edit', component: AuctionEditPage, canAccess: [MANAGER]},
    {path: '/my_biddings', component: BiddingTablePage, canAccess: [CLIENT]},
    {path: '/my_biddings/:auctionId', component: BiddingDetailsPage, canAccess: [CLIENT]},
    {path: '/403', component: ForbiddenPage, public: true},
    {path: '/404', component: NotFoundPage, public: true},
    {path: '/500', component: InternalServerErrorPage, public: true},
    {path: '*', component: NotFoundPage, public: true}
];