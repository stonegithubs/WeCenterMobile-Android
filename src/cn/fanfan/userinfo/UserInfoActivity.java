package cn.fanfan.userinfo;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.fanfan.attentionuser.AttentionUser;
import cn.fanfan.common.AsyncImageGet;
import cn.fanfan.common.ImageFileUtils;
import cn.fanfan.common.NetworkState;
import cn.fanfan.common.TipsToast;
import cn.fanfan.main.R;
import cn.fanfan.topic.TopicActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity implements OnClickListener {
	private static TipsToast tipsToast;
	private String uid;
	private ImageView iv_avatar;
	private TextView tv_username;
	private LinearLayout lv_topics;
	private TextView tv_topic;
	private LinearLayout lv_ifocus_person;
	private TextView tv_ifocus_person;
	private LinearLayout lv_focusi_person;
	private TextView tv_focusi_person;
	private TextView tv_thanks;
	private TextView tv_votes;
	private TextView tv_shared;
	private TextView tv_collect;
	private LinearLayout lv_replys;
	private TextView tv_replys;
	private LinearLayout lv_asks;
	private TextView tv_asks;
	private LinearLayout lv_articles;
	private TextView tv_articles;
	private LinearLayout lv_news;
	private TextView tv_news;
	private LinearLayout lv_search_friens;
	protected String errno;
	protected String err;
	protected String user_name;
	protected String avatar_file = "null";
	protected String fans_count;
	protected String friend_count;
	protected String question_count;
	protected String answer_count;
	protected String topic_focus_count;
	protected String agree_count;
	protected String thanks_count;
	protected String answer_favorite_count;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinformation_main);
		Intent intent = this.getIntent();
		// Bundle bundle = intent.getExtras();
		uid = intent.getStringExtra("uid");
		init();// 初始化
		if (uid != null) {
			NetworkState networkState = new NetworkState();
			if (networkState.isNetworkConnected(UserInfoActivity.this)) {
				getUserInfo();
			} else {
				showTips(R.drawable.tips_error, R.string.net_break);
			}

		} else {
			Toast.makeText(UserInfoActivity.this, "未登录请先登录", Toast.LENGTH_SHORT)
					.show();
			/* uid为null 跳转至登录Activity */
			// Intent intent = new Intent();
			// intent.setClass(MainActivity.this, UserInfo.class);
			// intent.putExtra("uid", "2");
			// startActivity(intent);
		}
	}

	protected void onResume() {
		super.onResume();
		if (uid != null) {
			NetworkState networkState = new NetworkState();
			if (networkState.isNetworkConnected(UserInfoActivity.this)) {
				getUserInfo();
			} else {
				showTips(R.drawable.tips_error, R.string.net_break);
			}
		}
	}

	private void getUserInfo() {
		// TODO Auto-generated method stub
		AsyncHttpClient getUserInfo = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		getUserInfo.get("http://w.hihwei.com/api/user.php", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						// TODO Auto-generated method stub
						// get请求成功后处理json。
						String responseContent = new String(responseBody);
						Log.i("getUserInfo", responseContent + "---Success");
						JSONTokener jsonParser = new JSONTokener(
								responseContent);
						try {
							JSONObject result = (JSONObject) jsonParser
									.nextValue();
							errno = result.getString("errno");
							err = result.getString("err");
							Log.i("errno", errno);
							Log.i("err", err);
							JSONObject rsm = new JSONObject();
							rsm = result.getJSONObject("rsm");
							JSONTokener jsonParser2 = new JSONTokener(rsm
									.toString());
							JSONObject rsmcontent = (JSONObject) jsonParser2
									.nextValue();
							user_name = rsmcontent.getString("user_name");
							avatar_file = rsmcontent.getString("avatar_file");
							Log.i("avatar_file", avatar_file);
							fans_count = rsmcontent.getString("fans_count");
							friend_count = rsmcontent.getString("friend_count");
							question_count = rsmcontent
									.getString("question_count");
							topic_focus_count = rsmcontent
									.getString("topic_focus_count");
							agree_count = rsmcontent.getString("agree_count");
							thanks_count = rsmcontent.getString("thanks_count");
							answer_favorite_count = rsmcontent
									.getString("answer_favorite_count");
							answer_count = rsmcontent.getString("answer_count");
							Log.i("answer_favorite_count",
									answer_favorite_count);
							// 处理完json后下载用户头像
							// AsyncImageGet asyncImageGet = new
							// AsyncImageGet(avatar_file,
							// iv_avatar);
							// asyncImageGet.execute();
							// updateUI展示用户资料
							// getUserAvatar(avatar_file);
							updateUI(avatar_file);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						showTips(R.drawable.tips_error, R.string.get_user_info);
					}
				});
	}


	protected void updateUI(String avatarurl) {
		// TODO Auto-generated method stub
		tv_username.setText(user_name);
		tv_focusi_person.setText(fans_count);
		tv_ifocus_person.setText(friend_count);
		tv_topic.setText(topic_focus_count);
		tv_votes.setText(agree_count);
		tv_thanks.setText(thanks_count);
		tv_collect.setText(answer_favorite_count);
		tv_replys.setText(answer_count);
		tv_asks.setText(question_count);
		if (avatarurl!="null") {
			AsyncImageGet getAvatar = new AsyncImageGet("http://w.hihwei.com/uploads/avatar/"+avatarurl, iv_avatar);
			getAvatar.execute();
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		tv_username = (TextView) findViewById(R.id.tv_username);
		lv_topics = (LinearLayout) findViewById(R.id.lv_topics);
		lv_topics.setOnClickListener(this);
		tv_topic = (TextView) findViewById(R.id.tv_topic);
		lv_ifocus_person = (LinearLayout) findViewById(R.id.lv_ifocus_person);
		lv_ifocus_person.setOnClickListener(this);
		tv_ifocus_person = (TextView) findViewById(R.id.tv_ifocus_person);
		lv_focusi_person = (LinearLayout) findViewById(R.id.lv_focusi_person);
		lv_focusi_person.setOnClickListener(this);
		tv_focusi_person = (TextView) findViewById(R.id.tv_focusi_person);

		tv_thanks = (TextView) findViewById(R.id.tv_thanks);
		tv_votes = (TextView) findViewById(R.id.tv_votes);
		tv_shared = (TextView) findViewById(R.id.tv_shared);
		tv_collect = (TextView) findViewById(R.id.tv_collect);

		lv_replys = (LinearLayout) findViewById(R.id.lv_replys);
		lv_replys.setOnClickListener(this);
		tv_replys = (TextView) findViewById(R.id.tv_replys);
		lv_asks = (LinearLayout) findViewById(R.id.lv_asks);
		lv_asks.setOnClickListener(this);
		tv_asks = (TextView) findViewById(R.id.tv_asks);
		lv_articles = (LinearLayout) findViewById(R.id.lv_articles);
		lv_articles.setOnClickListener(this);
		tv_articles = (TextView) findViewById(R.id.tv_articles);

		lv_news = (LinearLayout) findViewById(R.id.lv_news);
		lv_news.setOnClickListener(this);
		tv_news = (TextView) findViewById(R.id.tv_news);

		lv_search_friens = (LinearLayout) findViewById(R.id.lv_search_friens);
		lv_search_friens.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lv_topics:
			Intent intent = new Intent(UserInfoActivity.this,TopicActivity.class);
			startActivity(intent);
			break;
		case R.id.lv_focusi_person:
			Toast.makeText(UserInfoActivity.this, "lv_focusi_person",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lv_ifocus_person:
			Intent intent1 = new Intent(UserInfoActivity.this,AttentionUser.class);
			startActivity(intent1);
			break;
		case R.id.lv_articles:
			Toast.makeText(UserInfoActivity.this, "lv_articles",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lv_asks:
			Toast.makeText(UserInfoActivity.this, "lv_asks", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.lv_news:
			Toast.makeText(UserInfoActivity.this, "lv_news", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.lv_search_friens:
			Toast.makeText(UserInfoActivity.this, "lv_search_friens",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lv_replys:
			Toast.makeText(UserInfoActivity.this, "lv_replys",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private void showTips(int iconResId, int msgResId) {
		if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = TipsToast.makeText(getApplication().getBaseContext(),
					msgResId, TipsToast.LENGTH_SHORT);
		}
		tipsToast.show();
		tipsToast.setIcon(iconResId);
		tipsToast.setText(msgResId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.userinforedit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.edit) {
			Intent intent = new Intent(UserInfoActivity.this,
					UserInfoEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("uid", uid);
			bundle.putString("avatar_file", avatar_file);
			intent.putExtras(bundle);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
