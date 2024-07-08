package ww.smartexpress.app.utils;

import java.util.HashMap;
import java.util.Map;

import ww.smartexpress.app.constant.ErrorCode;

public class ErrorUtils {
    Map<String, String> errorMessage = new HashMap<>();
    public ErrorUtils(){

        /**
         * Starting error code Category
         */
        errorMessage.put(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Không tìm thấy dịch vụ");
        errorMessage.put(ErrorCode.CATEGORY_ERROR_NAME_EXIST, "Dịch vụ không tồn tại");

        /**
         * Starting error code Service
         */
        errorMessage.put(ErrorCode.SERVICE_ERROR_NOT_FOUND, "Không tìm thấy dịch vụ");
        errorMessage.put(ErrorCode.SERVICE_ERROR_SERVICE_EXIST, "Dịch vụ không tồn tại");
        errorMessage.put(ErrorCode.SERVICE_ERROR_CATEGORY_NOT_FOUND, "Dịch vụ không tồn tại");
        errorMessage.put(ErrorCode.SERVICE_ERROR_NAME_EXIST, "Dịch vụ không tồn tại");

        /**
         * Starting error code Driver
         */
        errorMessage.put(ErrorCode.DRIVER_ERROR_NOT_FOUND, "Không tìm thấy tài xế");
        errorMessage.put(ErrorCode.DRIVER_ERROR_PHONE_EXIST, "Số điện thoại tài xế không tồn tại");
        errorMessage.put(ErrorCode.DRIVER_ERROR_LOGIN_FAILED, "Đăng nhập thất bại");
        errorMessage.put(ErrorCode.DRIVER_ERROR_WRONG_PASSWORD, "Sai mật khẩu. Vui lòng thử lại");
        errorMessage.put(ErrorCode.DRIVER_ERROR_EMAIL_NOT_FOUND, "Không tìm thấy email");
        errorMessage.put(ErrorCode.DRIVER_ERROR_OTP_INVALID, "OTP không hợp lệ");
        errorMessage.put(ErrorCode.DRIVER_ERROR_EXPIRED_TIME, "");
        errorMessage.put(ErrorCode.DRIVER_ERROR_NOT_ACTIVE, "Tài khoản tài xế chưa kích hoạt");
        errorMessage.put(ErrorCode.DRIVER_ERROR_POSITION_IS_BUSY, "Tài xế đang bận");
        errorMessage.put(ErrorCode.DRIVER_ERROR_STATE_OFF, "Tài xế không online");
        errorMessage.put(ErrorCode.DRIVER_ERROR_BANK_CARD_INVALID, "Thẻ ngân hàng không hợp lệ");
        errorMessage.put(ErrorCode.DRIVER_ERROR_IDENTIFICATION_CARD_INVALID, "CMND không hợp lệ");
        errorMessage.put(ErrorCode.DRIVER_ERROR_TOTP_MISSING, "");
        errorMessage.put(ErrorCode.DRIVER_ERROR_TOTP_INVALID, "");
        errorMessage.put(ErrorCode.DRIVER_ERROR_ENABLED_2FA, "");

        /**
         * Starting error code Driver Service
         */
        errorMessage.put(ErrorCode.DRIVER_SERVICE_ERROR_NOT_FOUND, "Không tìm thấy dịch vụ");
        errorMessage.put(ErrorCode.DRIVER_SERVICE_ERROR_DRIVER_NOT_FOUND, "Không tìm thấy dịch vụ");
        errorMessage.put(ErrorCode.DRIVER_SERVICE_ERROR_SERVICE_NOT_FOUND, "Không tìm thấy dịch vụ");
        errorMessage.put(ErrorCode.DRIVER_SERVICE_ERROR_DRIVER_EXIST_SERVICE, "Dịch vụ không tồn tại");
        errorMessage.put(ErrorCode.DRIVER_SERVICE_ERROR_STATE_OFF, "Dịch vụ hiện không hoạt động");
        errorMessage.put(ErrorCode.DRIVER_SERVICE_ERROR_HAVE_OTHER_BOOKING, "Tài xế hiện đang có chuyến đi");
        errorMessage.put(ErrorCode.DRIVER_SERVICE_ERROR_NOT_ACTIVE, "Dịch vụ không được kích hoạt");
        errorMessage.put(ErrorCode.DRIVER_SERVICE_ERROR_POSITION_IS_BUSY, "Tài xế đang bận");

        /**
         * Starting error code Position
         */
        errorMessage.put(ErrorCode.POSITION_ERROR_NOT_FOUND, "Không tìm thấy vị trí");
        errorMessage.put(ErrorCode.POSITION_ERROR_EXIST, "Vị trí không tồn tại");
        errorMessage.put(ErrorCode.POSITION_ERROR_LATITUDE_OR_LONGITUDE_NULL, "Vị trí không hợp lệ");

        /**
         * Starting error code Driver Vehicle
         */
        errorMessage.put(ErrorCode.DRIVER_VEHICLE_ERROR_NOT_FOUND, "Không tìm thấy phương tiện");
        errorMessage.put(ErrorCode.DRIVER_VEHICLE_ERROR_EXIST, "Phương tiện không tồn tại");
        errorMessage.put(ErrorCode.DRIVER_VEHICLE_ERROR_PLATE_EXIST, "Biển số không tồn tại");
        errorMessage.put(ErrorCode.DRIVER_VEHICLE_ERROR_LICENSE_NO_EXIST, "Bằng lái không tồn tại");
        errorMessage.put(ErrorCode.DRIVER_VEHICLE_ERROR_DRIVER_NOT_FOUND, "Không tìm thấy tài xế của phương tiện");

        /**
         * Starting error code Service Customer
         */
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Không tìm thấy tài khoản khách hàng");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_PHONE_EXIST, "Số điện thoại không tồn tại");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_EMAIL_EXIST, "Địa chỉ email không tồn tại");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_LOGIN_FAILED, "Số điện thoại hoặc mật khẩu không đúng");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_WRONG_PASSWORD, "Sai mật khẩu. Vui lòng thử lại");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_EMAIL_NOT_FOUND, "Không tìm thấy địa chỉ emal");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_OTP_INVALID, "OTP không hợp lệ");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_EXPIRED_TIME, "OTP không hợp lệ");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_NOT_ACTIVE, "Tài khoản chưa kích hoạt. Vui lòng kích hoạt tài khoản");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_BANK_CARD_INVALID, "Thẻ ngân hàng không hợp lệ");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_STATUS_NOT_PENDING, "");
        errorMessage.put(ErrorCode.CUSTOMER_ERROR_STATUS_PENDING, "Tài khoản chưa kích hoạt. Vui lòng kích hoạt tài khoản");

        /**
         * Starting error code Nation
         * */
        errorMessage.put(ErrorCode.NATION_ERROR_NOT_FOUND, "Không tìm thấy quốc gia");
        errorMessage.put(ErrorCode.NATION_ERROR_POSTCODE_EXIST, "Mã POSTCODE không tồn tại");
        errorMessage.put(ErrorCode.NATION_ERROR_CANT_DELETE_RELATIONSHIP_WITH_DRIVER, "");

        /**
         * Starting error code Booking
         * */
        errorMessage.put(ErrorCode.BOOKING_ERROR_NOT_FOUND, "Không tìm thấy chuyến đi");
        errorMessage.put(ErrorCode.BOOKING_ERROR_NOT_ALLOW_CANCEL_WHEN_PICKED_UP, "Không thể hủy chuyến khi tài xế đã đón");
        errorMessage.put(ErrorCode.BOOKING_ERROR_UPDATE_STATE_NOT_VALID, "Xảy ra lỗi. Vui lòng thử lại");
        errorMessage.put(ErrorCode.BOOKING_ERROR_NOT_ALLOW_DELETE, "Không thể xóa chuyến đi");
        errorMessage.put(ErrorCode.BOOKING_ERROR_CANCELED_BOOKING, "Không thể hủy chuyến đi");
        errorMessage.put(ErrorCode.BOOKING_ERROR_NOT_ALLOW_DRIVER_UPDATE_STATE_CANCEL_OR_BOOKING, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.BOOKING_ERROR_NOT_ALLOW_UPDATE_DRIVER, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.BOOKING_ERROR_HAVE_OTHER_BOOKING, "Đang có chuyến");
        errorMessage.put(ErrorCode.BOOKING_ERROR_REVENUE_STATISTIC_EXCEED_NUMBER_OF_DAYS, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.BOOKING_ERROR_REVENUE_STATISTIC_START_DATE_MUST_BEFORE_END_DATE, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.BOOKING_ERROR_REVENUE_STATISTIC_INVALID_DATE_FORMAT, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.BOOKING_ERROR_PAYMENT_MONEY_GREATER_THAN_BALANCE_IN_WALLET, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.BOOKING_ERROR_HOLDING_MONEY_GREATER_THAN_BALANCE_IN_WALLET, "Xảy ra lỗi, vui lòng thử lại");

        /**
         * Starting error code Booking Detail
         * */
        errorMessage.put(ErrorCode.BOOKING_DETAIL_ERROR_NOT_FOUND, "Không tìm thấy thông tin chuyến đi");

        /**
         * Starting error code Room
         */
        errorMessage.put(ErrorCode.ROOM_ERROR_NOT_FOUND, "Không tìm thấy đoạn hội thoại");
        errorMessage.put(ErrorCode.ROOM_ERROR_EXIST, "Không tìm thấy đoạn hội thoại");
        errorMessage.put(ErrorCode.ROOM_ERROR_BOOKING_NOT_FOUND, "Không tìm thấy chuyến đi");
        errorMessage.put(ErrorCode.ROOM_ERROR_BOOKING_STATE_NOT_VALID, "Chuyến đi không hợp lệ");
        errorMessage.put(ErrorCode.ROOM_ERROR_DRIVER_NOT_FOUND_IN_BOOKING, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.ROOM_ERROR_CUSTOMER_NOT_FOUND_IN_BOOKING, "Xảy ra lỗi, vui lòng thử lại");

        /**
         * Starting error code Chat Detail
         */
        errorMessage.put(ErrorCode.CHAT_DETAIL_ERROR_NOT_FOUND, "Không tìm thấy đoạn hội thoại");
        errorMessage.put(ErrorCode.CHAT_DETAIL_ERROR_CAN_NOT_UPDATE, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.CHAT_DETAIL_ERROR_NOT_TYPE, "Xảy ra lỗi, vui lòng thử lại");

        /**
         * Starting error code Sender
         */
        errorMessage.put(ErrorCode.SENDER_ERROR_NOT_FOUND, "Không tìm thấy người gửi");

        /**
         * Starting error code Rating
         */
        errorMessage.put(ErrorCode.RATING_ERROR_NOT_FOUND, "Không tìm thấy đánh giá");
        errorMessage.put(ErrorCode.RATING_ERROR_EXIST, "Đánh giá không tồn tại");
        errorMessage.put(ErrorCode.RATING_ERROR_BOOKING_NOT_DONE, "Xảy ra lỗi, vui lòng thử lại");

        /**
         * Starting error code Promotion
         */
        errorMessage.put(ErrorCode.PROMOTION_ERROR_NOT_FOUND, "Không tìm thấy khuyến mãi");
        errorMessage.put(ErrorCode.PROMOTION_ERROR_STATE_INVALID, "Khuyến mãi không hợp lệ");
        errorMessage.put(ErrorCode.PROMOTION_ERROR_QUANTITY_CAN_NOT_UPDATED, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.PROMOTION_ERROR_USED, "Bạn đã sử dụng mã khuyến mãi này");

        /**
         * Starting error code Promotion Code
         */
        errorMessage.put(ErrorCode.PROMOTION_CODE_ERROR_USED_UP, "Xảy ra lỗi, vui lòng thử lại");

        /**
         * Starting error code Wallet
         */
        errorMessage.put(ErrorCode.WALLET_ERROR_NOT_FOUND, "Không tìm thấy ví. Vui lòng thử lại");
        errorMessage.put(ErrorCode.WALLET_ERROR_KIND_INVALID, "Xảy ra lỗi, vui lòng thử lại");

        /**
         * Starting error code Wallet Transaction
         */
        errorMessage.put(ErrorCode.WALLET_TRANSACTION_ERROR_NOT_FOUND, "Không tìm thấy giao dịch");
        errorMessage.put(ErrorCode.WALLET_TRANSACTION_ERROR_MONEY_INVALID, "Xảy ra lỗi, vui lòng thử lại");

        /**
         * Starting error code Request Pay Out
         */
        errorMessage.put(ErrorCode.REQUEST_PAY_OUT_ERROR_NOT_FOUND, "Không tìm thấy yêu cầu tút tiền");
        errorMessage.put(ErrorCode.REQUEST_PAY_OUT_ERROR_STATE_INVALID, "Yêu cầu rút tiền không hợp lệ");
        errorMessage.put(ErrorCode.REQUEST_PAY_OUT_ERROR_BANK_CARD_INVALID, "Yêu cầu rút tiền không hợp lệ");
        errorMessage.put(ErrorCode.REQUEST_PAY_OUT_ERROR_TRANSACTION_CODE_OR_IMAGE_NULL, "Yêu cầu rút tiền không hợp lệ");
        errorMessage.put(ErrorCode.REQUEST_PAY_OUT_ERROR_NOTE_NULL, "Yêu cầu rút tiền không hợp lệ");
        errorMessage.put(ErrorCode.REQUEST_PAY_OUT_ERROR_MONEY_WITHDRAW_GREATER_THAN_BALANCE_IN_WALLET, "Yêu cầu rút tiền không hợp lệ");
        errorMessage.put(ErrorCode.REQUEST_PAY_OUT_ERROR_NOT_ALLOW_DELETE_WHEN_APPROVED_OR_REJECTED, "Yêu cầu rút tiền không hợp lệ");
        errorMessage.put(ErrorCode.REQUEST_PAY_OUT_ERROR_HAVE_REQUEST_PAY_OUT_WITH_STATE_INIT, "Yêu cầu rút tiền không hợp lệ");
        errorMessage.put(ErrorCode.REQUEST_PAY_OUT_ERROR_USER_NOT_HAVE_THIS_REQUEST_PAY_OUT, "Yêu cầu rút tiền không hợp lệ");

        /**
         * Starting error code Settings
         * */
        errorMessage.put(ErrorCode.SETTINGS_ERROR_NOT_FOUND, "Không tìm thấy cài đặt");
        errorMessage.put(ErrorCode.SETTINGS_ERROR_SETTING_KEY_EXISTED, "Không tìm thấy cài đặt");

        /**
         * Starting error code Notification
         */
        errorMessage.put(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND, "Không tìm thấy thông báo");
        errorMessage.put(ErrorCode.NOTIFICATION_USER_ERROR_NOT_FOUND, "Không tìm thấy thông báo");
        errorMessage.put(ErrorCode.OTP_ERROR_NOT_CORRECT_OR_EXPIRED, "OTP không hợp lệ");

        /**
         * Starting error code News
         */
        errorMessage.put(ErrorCode.NEWS_ERROR_NOT_FOUND, "Không tìm thấy bản tin");
        errorMessage.put(ErrorCode.NEWS_ERROR_TARGET_USER_NOT_FOUND, "Xảy ra lỗi, vui lòng thử lại");
        errorMessage.put(ErrorCode.NEWS_ERROR_ALREADY_PUBLISHED, "Xảy ra lỗi, vui lòng thử lại");

    }

    public String handelError(String message){
        return errorMessage.get(message);
    }
}
