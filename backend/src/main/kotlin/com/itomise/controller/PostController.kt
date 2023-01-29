package com.itomise.com.itomise.controller

import com.itomise.com.itomise.controller.requestModels.CreatePostRequestModel
import com.itomise.com.itomise.controller.requestModels.PublishPostRequestModel
import com.itomise.com.itomise.controller.requestModels.UnPublishPostRequestModel
import com.itomise.com.itomise.controller.requestModels.UpdatePostRequestModel
import com.itomise.com.itomise.controller.responseModels.GetListPostResponseModel
import com.itomise.com.itomise.controller.responseModels.GetPostResponseModel
import com.itomise.com.itomise.usecase.interfaces.post.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.postRouting() {
    val getListPostUseCase by inject<IGetListPostUseCase>()
    val getPostUseCase by inject<IGetPostUseCase>()
    val createPostUseCase by inject<ICreatePostUseCase>()
    val updatePostUseCase by inject<IUpdatePostUseCase>()
    val deletePostUseCase by inject<IDeletePostUseCase>()
    val publishPostUseCase by inject<IPublishPostUseCase>()
    val unPublishPostUseCase by inject<IUnPublishPostUseCase>()

    authenticate("auth-session") {
        route("/posts") {
            get {
                val result = getListPostUseCase.handle()

                call.respond(HttpStatusCode.OK, GetListPostResponseModel(
                    posts = result.map {
                        GetPostResponseModel(
                            id = it.id,
                            title = it.title,
                            content = it.content,
                            status = it.status
                        )
                    }
                ))
            }
            post {
                val request = call.receive<CreatePostRequestModel>()
                createPostUseCase.handle(
                    ICreatePostUseCase.Command(
                        title = request.title,
                        content = request.content
                    )
                )

                call.respond(HttpStatusCode.OK)
            }
            route("/{postId}") {
                get {
                    val postId = call.parameters["postId"] ?: throw IllegalArgumentException()

                    val result = getPostUseCase.handle(UUID.fromString(postId))

                    if (result != null) {
                        call.respond(
                            HttpStatusCode.OK, GetPostResponseModel(
                                id = result.id,
                                title = result.title,
                                content = result.content,
                                status = result.status
                            )
                        )
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
                put {
                    val postId = call.parameters["postId"] ?: throw IllegalArgumentException()
                    val request = call.receive<UpdatePostRequestModel>()

                    updatePostUseCase.handle(
                        IUpdatePostUseCase.Command(
                            id = UUID.fromString(postId),
                            title = request.title,
                            content = request.content,
                        )
                    )

                    call.respond(HttpStatusCode.OK)
                }
                delete {
                    val postId = call.parameters["postId"] ?: throw IllegalArgumentException()

                    deletePostUseCase.handle(UUID.fromString(postId))

                    call.respond(HttpStatusCode.OK)
                }

                put("/publish") {
                    val postId = call.parameters["postId"] ?: throw IllegalArgumentException()
                    val request = call.receive<PublishPostRequestModel>()

                    updatePostUseCase.handle(
                        IUpdatePostUseCase.Command(
                            id = UUID.fromString(postId),
                            title = request.title,
                            content = request.content
                        )
                    )

                    publishPostUseCase.handle(UUID.fromString(postId))

                    call.respond(HttpStatusCode.OK)
                }
                put("/un-publish") {
                    val postId = call.parameters["postId"] ?: throw IllegalArgumentException()
                    val request = call.receive<UnPublishPostRequestModel>()

                    updatePostUseCase.handle(
                        IUpdatePostUseCase.Command(
                            id = UUID.fromString(postId),
                            title = request.title,
                            content = request.content
                        )
                    )

                    unPublishPostUseCase.handle(UUID.fromString(postId))

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}