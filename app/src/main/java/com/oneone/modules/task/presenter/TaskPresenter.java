package com.oneone.modules.task.presenter;

import android.os.AsyncTask;

import com.oneone.BasePresenter;
import com.oneone.modules.task.dto.LoginReceiveAwardDTO;
import com.oneone.modules.task.contract.TaskContract;
import com.oneone.modules.task.model.TaskModel;
import com.oneone.restful.ApiResult;

/**
 * Created by here on 18/5/2.
 */

public class TaskPresenter extends BasePresenter<TaskContract.View> implements TaskContract.Presenter {
    private TaskModel taskModel;

    @Override
    public void onAttachView(TaskContract.View view) {
        super.onAttachView(view);
        taskModel = new TaskModel(view.getActivityContext());
    }

    @Override
    public void taskLoginReceiveAward() {
        enqueue(new AsyncTask<Object, Void, ApiResult<LoginReceiveAwardDTO>>() {

            @Override
            protected ApiResult<LoginReceiveAwardDTO> doInBackground(Object... voids) {
                return taskModel.taskLoginReceiveAward();
            }
            @Override
            protected void onPostExecute(ApiResult<LoginReceiveAwardDTO> result) {
                super.onPostExecute(result);
                if (result != null && result.getData() != null) {
                    getView().onTaskLoginReceiveAward(result.getData().getTaskAward(), result.getData().getReceived());
                }
            }
        });
    }
}
