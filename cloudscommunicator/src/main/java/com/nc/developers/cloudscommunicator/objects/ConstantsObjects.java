package com.nc.developers.cloudscommunicator.objects;

public class ConstantsObjects{
    //action array strings
    public static final String ADD_MEMBER="ADD_MEMBER";
    public static final String FETCH_INVITEE="FETCH_INVITEE";
    public static final String REMOVE_MEMBER="REMOVE_MEMBER";
    public static final String SNIP_MESSAGE="SNIP_MESSAGE";
    public static final String GET_ROLE_TASK="GET_ROLE_TASK";
    public static final String FETCH_ALL_CONTACT="FETCH_ALL_CONTACT";
    public static final String FETCH_CONTACT_USERS="FETCH_CONTACT_USERS";
    public static final String ADD_CONTACT="ADD_CONTACT";
    public static final String DELETE_CONTACT="DELETE_CONTACT";
    public static final String REGISTER="REGISTER";
    public static final String FETCH_SPECIFIC_GROUP="FETCH_SPECIFIC_GROUP";
    public static final String ADD_MESSAGING_CONTACT="ADD_MESSAGING_CONTACT";
    public static final String CLEAR_CHAT="CLEAR_CHAT";
    public static final String ARCHIVE_CONVERSATION="ARCHIVE_CONVERSATION";
    public static final String FETCH_CONVERSATION_FILTER="FETCH_CONVERSATION_FILTER";
    public static final String CAST_CONVERSATION="CAST_CONVERSATION";
    public static final String FETCH_MESSAGE_FILTER="FETCH_MESSAGE_FILTER";
    public static final String ADD_MESSAGE="ADD_MESSAGE";
    public static final String MAKE_ADMIN="MAKE_ADMIN";
    public static final String REMOVE_ADMIN="REMOVE_ADMIN";
    public static final String UPDATE_CONVERSATION="UPDATE_CONVERSATION";
    public static final String SEARCH_ALL_CONTACT="SEARCH_ALL_CONTACT";
    public static final String SNIP_CONVERSATION="SNIP_CONVERSATION";
    public static final String ERASE_CONVERSATION="ERASE_CONVERSATION";
    public static final String REMOVE_MEMBER_SERVER="REMOVE_MEMBER_SERVER";
    public static final String UNARCHIVE_CONVERSATION="UNARCHIVE_CONVERSATION";
    public static final String NESTED_MESSAGE="NESTED_MESSAGE";
    public static final String FETCH_NESTED_MESSAGE="FETCH_NESTED_MESSAGE";
    public static final String FORWARD_MESSAGE="FORWARD_MESSAGE";
    public static final String ADD_BLOCK_CONTACT="ADD_BLOCK_CONTACT";
    public static final String UPDATE_USER="UPDATE_USER";
    public static final String EDIT_MESSAGE="EDIT_MESSAGE";
    public static final String UNBLOCK_CONTACT="UNBLOCK_CONTACT";
    public static final String FETCH_CONTACT="FETCH_CONTACT";
    public static final String SEARCH_MESSAGE="SEARCH_MESSAGE";
    public static final String SUPERIMPOSE_EVENT="SUPERIMPOSE_EVENT";
    public static final String CAST_EVENT="CAST_EVENT";

    //keytype
    public static final String KEY_TYPE="keyType";
    public static final String KEY_TYPE_INNER="KEY_TYPE";
    public static final String PARENT_KEY_TYPE="PARENT_KEY_TYPE";

    //keytype values
    public static final String KT_TSK="TSK";
    public static final String FETCH_ROLE_KT="TSK";
    public static final String KT_FETCH_INVITEE="IDE";
    public static final String KT_ADD_INVITEE="IDE";
    public static final String KT_ADD_REMOVE_INVITEE="TSK";
    public static final String KT_DELETE_CONVERSATION="IDE";
    public static final String KT_DELETE_CONVERSATION2="TSK";
    public static final String KT_FETCH_ALL_CONTACTS="TSK";
    public static final String KT_SEARCH_CONTACTS="PRJ";
    public static final String KT_FETCH_SPECIFIC_GROUP="TSK";
    public static final String KT_SEND_MESSAGE="TSK";
    public static final String KT_INVITEE="IDE";
    public static final String KT_CONTACT="TSK";
    public static final String KT_CONVERSATION="TSK";

    //subkeytype
    public static final String SUB_KEY_TYPE="subKeyType";
    public static final String SUB_KEY_TYPE_INNER="SUB_KEY_TYPE";
    public static final String PARENT_SUB_KEY_TYPE="PARENT_SUB_KEY_TYPE";
    public static final String FETCH_ROLE_SUB_KT="TSK_ROL_LST";
    public static final String SUB_KEY_TYPE_ARRAY="subKeyTypeArray";

    //subkeytype values
    public static final String SUB_KT_FETCH_CONTACT="TSK_CDE";
    public static final String SUB_KT_ADD_CONTACT="TSK_CDE";
    public static final String SUB_KT_SEARCH_CONTACTS="PRJ_ORG";
    public static final String SUB_KT_MESSAGE="TSK_MSG";
    public static final String SUB_KT_SEND_MESSAGE_ATTACHMENT="TSK_CONV_ATH";
    public static final String SUB_KT_CONTACT="TSK_CDE";
    public static final String SUB_KT_BLOCKED_CONTACT="TSK_BLK_CDE";
    public static final String SUB_KT_CONVERSATION="CONV";
    public static final String SUB_KT_CONVERSATION_ONE_TO_ONE="TSK_SCONV_LST";
    public static final String SUB_KT_CONVERSATION_ARRAY1="TSK_CONV_LST";
    public static final String SUB_KT_INVITEE="TSK_CONV_LST_IDE";
    public static final String SUB_KT_BLOCK_MESSAGING_CONTACT="TSK_BLK_CDE";
    public static final String SUB_KT_CALENDAR_EVENT="TSK_CAL_EVT";

    //fields
    public static final String USER_ID="userId";
    public static final String ACTION_ARRAY="actionArray";
    public static final String ORG_ID="orgId";
    public static final String LAST_ID_D_CAP="lastID";
    public static final String PROJECT_ID="projectId";
    public static final String DATA_ARRAY="dataArray";
    public static final String REQUEST_ID="requestId";
    public static final String SOCKET_ID="socketId";
    public static final String MODULE_NAME="moduleName";
    public static final String CML_TITLE="CML_TITLE";
    public static final String FROM="FROM";
    public static final String CML_ACCEPTED="CML_ACCEPTED";
    public static final String CML_SUB_CATEGORY="CML_SUB_CATEGORY";
    public static final String LAST_MODIFIED_ON="LAST_MODIFIED_ON";
    public static final String CALMAIL_UPDATE="calmailUpdate";
    public static final String CALMAIL_OBJECT="calmailObject";
    public static final String CML_COMPLETION_PERCENTAGE="CML_COMPLETION_PERCENTAGE";
    public static final String CML_DESCRIPTION="CML_DESCRIPTION";
    public static final String CML_ESTIMATED_TIME="CML_ESTIMATED_TIME";
    public static final String CML_FIRST_NAME="CML_FIRST_NAME";
    public static final String CML_HOURS="CML_HOURS";
    public static final String CML_IMAGE_PATH="CML_IMAGE_PATH";
    public static final String CML_LAST_NAME="CML_LAST_NAME";
    public static final String CML_NICK_NAME="CML_NICK_NAME";
    public static final String CML_OFFICIAL_EMAIL="CML_OFFICIAL_EMAIL";
    public static final String CML_ORG_WEB_SITE="CML_ORG_WEB_SITE";
    public static final String CML_PERSONAL_EMAIL="CML_PERSONAL_EMAIL";
    public static final String CML_PRIORITY="CML_PRIORITY";
    public static final String CML_REF_ID="CML_REF_ID";
    public static final String CML_TAG="CML_TAG";
    public static final String CML_TEMP_KEY_VAL="CML_TEMP_KEY_VAL";
    public static final String CONTACT_ID="CONTACT_ID";
    public static final String DEPT_ID_INNER="DEPT_ID";
    public static final String ORG_ID_INNER="ORG_ID";
    public static final String ORG_PROJECT_ID_INNER="ORG_PROJECT_ID";
    public static final String DEPT_PROJECT_ID="DEPT_PROJECT_ID";
    public static final String PARENT_WIZARD_ID="PARENT_WIZARD_ID";
    public static final String SYNC_PENDING_STATUS="SYNC_PENDING_STATUS";
    public static final String USER_LAST_MODIFIED_ON="USER_LAST_MODIFIED_ON";
    public static final String ACTIVE_STATUS_DATA_ARRAY_INNER="activeStatus";
    public static final String FILTER_OBJECT="filterObject";
    public static final String FETCH_KEYS="fetchKeys";
    public static final String CONTACT_ID_INNER="contactId";
    public static final String DOMAIN_NAME="domainName";
    public static final String CML_IS_ACTIVE="CML_IS_ACTIVE";
    public static final String LAST_ID_OBJECT="lastIdObject";
    public static final String SORT_FIELD="sortField";
    public static final String SECTION_IDS="sectionIds";
    public static final String ADDED_BY="ADDED_BY";
    public static final String INVITEE_LIST="inviteeList";
    public static final String LAST_ELEMENT_ID="lastElementId";
    public static final String CML_PARENT_TEMP_KEY_VAL="CML_PARENT_TEMP_KEY_VAL";
    public static final String CML_TYPE="CML_TYPE";
    public static final String CML_PARENT_ID="CML_PARENT_ID";
    public static final String CML_PARENT_SUB_KEY_TYPE="CML_PARENT_SUB_KEY_TYPE";
    public static final String GROUP_ID="GROUP_ID";
    public static final String GROUP_IDS="GROUP_IDS";
    public static final String MESSAGE_ARRAY="MESSAGE_ARRAY";
    public static final String CML_MESSAGE_INDEX="CML_MESSAGE_INDEX";
    public static final String IS_CONTACT="isContact";
    public static final String LAST_NAME="lastName";
    public static final String RECOVERY_MAIL="RECOVERY_MAIL";
    public static final String REGISTER_WITH="RegisterWith";
    public static final String RPASSWORD="rPassword";
    public static final String FIRSTNAME="firstName";
    public static final String IS_MOBILE_APPLICATION_REQ="isMobileApplicationReq";
    public static final String ORG_OBJ="orgObj";
    public static final String APPLICATION="application";
    public static final String IS_ORG_DOMAIN_REGISTER="isOrgDomainRegister";
    public static final String TOPIC="topic";
    public static final String USERNAME="username";
    public static final String ACTION_ARRAY_REGISTER="ACTION_ARRAY";
    public static final String CONTACT_USER="CONTACT_USER";
    public static final String DESIGNATION="DESIGNATION";
    public static final String IMG_PATH="imgPath";
    public static final String EXTRA_PARAM_TYPE="type";
    public static final String CML_CHT_MESSAGE_TYPE="CML_CHT_MESSAGE_TYPE";
    public static final String IS_SINGLE_DELETE="isSingleDelete";
    public static final String DEPT_ID="deptId";
    public static final String LAST_ID="lastId";
    public static final String ORG_PROJECT_ID="orgProjectId";
    public static final String PARENT_ID="parentId";
    public static final String OFFSET="offset";
    public static final String TN="TN";
    public static final String IS_ATTACHMENT="IS_ATTACHMENT";
    public static final String IS_NESTED="IS_NESTED";
    public static final String SECTION_ID="sectionId";
    public static final String LINKUP_ID="LINKUP_ID";
    public static final String IS_CONTACT_INVITEE_UPDATE="IS_CONTACT";
    public static final String USER_KEY_VAL="USER_KEY_VAL";
    public static final String CML_ISREPLY="CML_ISREPLY";
    public static final String CML_PARENT_CREATED_BY="CML_PARENT_CREATED_BY";
    public static final String CML_PARENT_MSG_TITLE="CML_PARENT_MSG_TITLE";
    public static final String CML_PARENT_TASK_ID="CML_PARENT_TASK_ID";
    public static final String IS_PRIMARY="IS_PRIMARY";
    public static final String CML_FROM_TIME="CML_FROM_TIME";
    public static final String CML_TO_TIME="CML_TO_TIME";
    public static final String ACCEPTED_BY_ME="acceptedByMe";
    public static final String ARCHIVE_EVENT="archiveEvent";
    public static final String CREATED_BY_ME="createdByMe";
    public static final String CREATED_FOR_ME="createdForMe";
    public static final String MAY_BE_FILTER="mayBeFilter";
    public static final String PROJECT_IDS="projectIds";
    public static final String CML_ASSIGNED_TO="CML_ASSIGNED_TO";
    public static final String CML_CITY="CML_CITY";
    public static final String CML_CONTINENT="CML_CONTINENT";
    public static final String CML_COUNTRY="CML_COUNTRY";
    public static final String CML_COUNTRY_CODE="CML_COUNTRY_CODE";
    public static final String CML_FROM_DATETIME="CML_FROM_DATETIME";
    public static final String CML_LATITUDE="CML_LATITUDE";
    public static final String CML_LOCATION="CML_LOCATION";
    public static final String CML_LONGITUDE="CML_LONGITUDE";
    public static final String CML_MAIN_PARENT="CML_MAIN_PARENT";
    public static final String CML_PINCODE="CML_PINCODE";
    public static final String CML_STATE="CML_STATE";
    public static final String CML_TIMEZONE="CML_TIMEZONE";
    public static final String CML_TO_DATETIME="CML_TO_DATETIME";
    public static final String CML_ZIPCODE="CML_ZIPCODE";
    public static final String TYPE="TYPE";
    public static final String PARENT_CONVERSATION_ID="PARENT_CONVERSATION_ID";

    //role names
    public static final String ROLE_PERSONAL="Personal";
    public static final String ROLE_WORK="Work";
    public static final String ROLE_FAMILY="Family";
    public static final String ROLE_SOCIAL="Social";
    public static final String ROLE_UNASSIGNED="Unassigned";

    //field value
    public static final String FROM_VALUE="senddatatoserver";
    public static final String DOMAIN_VALUE="@clouzer.com";
    public static final String SORT_FIELD_VALUE="latest";
    public static final String IDE_DESIGNATION_VALUE="ADMIN";
    public static final String IDE_TYPE_VALUE_TO="TO";
    public static final String TOPIC_VALUE_REGISTER="REGISTER";
    public static final String EXTRA_PARAM_TYPE_INSERT="insert";

    //module name values
    public static final String MODULE_NAME_VALUE="CGR";
    public static final String MODULE_NAME_VALUE_CRM="CRM";
    public static final String MODULE_VALUE_CONTACT="CRM";
    public static final String MODULE_VALUE_CALENDAR_EVENT="EVT";
    public static final String MODULE_VALUE_ADD_CHAT="CGR";
    public static final String MODULE_VALUE_FETCH_MESSAGES="CGR";
    public static final String MODULE_VALUE_SEND_MESSAGES="CGR";
    public static final String MODULE_VALUE_FETCH_INVITEE="CGR";
    public static final String MODULE_VALUE_FETCH_SPECIFIC_GROUP="CGR";

    //action values
    public static final String UPDATE="UPDATE";
    public static final String UPDATE_UNREAD_COUNT="UPDATE_UNREAD_COUNT";
    public static final String CONVERSATION_LIST_ID="CONVERSATION_LIST_ID";
    public static final String LATEST_MESSAGE="LATEST_MESSAGE";
    public static final String ACTION_UPDATE="UPDATE";
    public static final String ACTION_DELETE="DELETE";
    public static final String ACTION_INSERT="INSERT";
    public static final String ESSENTIAL_LIST_ARRAY="essentialList";
    public static final String CREATORS_ID="CreatorsId";
    public static final String LAST_MODIFIED_BY="LAST_MODIFIED_BY";
    public static final String KEY_VAL="keyVal";
    public static final String ACTION="action";
    public static final String INVITATION_INSERT="invitationInsert";
    public static final String INVITATION_DELETE="invitationDelete";
    public static final String INVITATION_UPDATE="invitationUpdate";
    public static final String CML_ASSIGNED="CML_ASSIGNED";
    public static final String CML_IS_LATEST="CML_IS_LATEST";
    public static final String CML_STAR="CML_STAR";
    public static final String CML_UNREAD_COUNT="CML_UNREAD_COUNT";
    public static final String IDE_ACCEPTED="IDE_ACCEPTED";
    public static final String IDE_ATTENDEES_EMAIL="IDE_ATTENDEES_EMAIL";
    public static final String IDE_CML_ID="IDE_CML_ID";
    public static final String IDE_DESIGNATION="IDE_DESIGNATION";
    public static final String IDE_ORIGINAL_CREATOR="IDE_ORIGINAL_CREATOR";
    public static final String IDE_TYPE="IDE_TYPE";
    public static final String IDE_TYPE_VALUE_FROM="FROM";
    public static final String EXTRA_PARAM="extraParam";   //FunctionKey+Shift+F6
    public static final String HIT_SERVER_FLAG="hitServerFlag";
    public static final String ACTIVE_STATUS="ACTIVE_STATUS";
    public static final String CREATED_BY="CREATED_BY";
    public static final String CREATED_ON="CREATED_ON";
    public static final String CREATORS_ID_INNER="CREATORS_ID";
    public static final String INVITEE_CML_ID="INVITEE_CML_ID";
    public static final String KEY_VAL_INNER="KEY_VAL";
    public static final String DELETE_KEY="deleteKey";
    public static final String URM_PROJECT_ID="URM_PROJECT_ID";
    public static final String OLD_KEY_VAL="OLD_KEY_VAL";
    public static final String OWNER_ID="OWNER_ID";

    //emit values
    public static final String ON_DEMAND_CALL="OnDemandCall";
    public static final String SERVER_OPERATION="serverOperation";
}