RD_URL = "https://github.com/JetBrains/rd/archive/refs/tags/2026.3.2.zip"
RD_SHA256 = "a9c6b9d700692e9a4a052243339ad2d6c152825d1e4ffa68ba1ad0dff5802f55"
SPDLOG_URL = "https://github.com/gabime/spdlog/archive/128cbe5a06051e0bc43d90c2434f7f68a2900587.zip"
SPDLOG_SHA256 = "88ddaf58303882d77e15d9cbc24ccd2e2a702636003ef64d14764d355fcae6d5"

def _rd_cpp_repository_impl(repository_ctx):
    repository_ctx.download_and_extract(
        url = RD_URL,
        sha256 = RD_SHA256,
        stripPrefix = "rd-2026.3.2/rd-cpp",
    )
    repository_ctx.download_and_extract(
        url = SPDLOG_URL,
        output = "thirdparty/spdlog",
        sha256 = SPDLOG_SHA256,
        stripPrefix = "spdlog-128cbe5a06051e0bc43d90c2434f7f68a2900587",
    )
    repository_ctx.file(
        "BUILD.bazel",
        repository_ctx.read(repository_ctx.attr.build_file),
    )
    return repository_ctx.repo_metadata(reproducible = True)

rd_cpp_repository = repository_rule(
    implementation = _rd_cpp_repository_impl,
    attrs = {
        "build_file": attr.label(
            mandatory = True,
            allow_single_file = True,
        ),
    },
)
