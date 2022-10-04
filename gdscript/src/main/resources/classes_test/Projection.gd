class_name Projection

const PLANE_NEAR = 0;

const PLANE_FAR = 1;

const PLANE_LEFT = 2;

const PLANE_TOP = 3;

const PLANE_RIGHT = 4;

const PLANE_BOTTOM = 5;

const IDENTITY = Projection(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

const ZERO = Projection(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);


var w: Vector4;

var x: Vector4;

var y: Vector4;

var z: Vector4;


func Projection() -> Projection:
	pass;

func Projection(from: Projection) -> Projection:
	pass;

func Projection(from: Transform3D) -> Projection:
	pass;

#desc Constructs a Projection from four [Vector4] values (matrix columns).
func Projection(x_axis: Vector4, y_axis: Vector4, z_axis: Vector4, w_axis: Vector4) -> Projection:
	pass;


static func create_depth_correction(flip_y: bool) -> Projection:
	pass;

static func create_fit_aabb(aabb: AABB) -> Projection:
	pass;

static func create_for_hmd(eye: int, aspect: float, intraocular_dist: float, display_width: float, display_to_lens: float, oversample: float, z_near: float, z_far: float) -> Projection:
	pass;

static func create_frustum(left: float, right: float, bottom: float, top: float, z_near: float, z_far: float) -> Projection:
	pass;

static func create_frustum_aspect(size: float, aspect: float, offset: Vector2, z_near: float, z_far: float, flip_fov: bool) -> Projection:
	pass;

static func create_light_atlas_rect(rect: Rect2) -> Projection:
	pass;

static func create_orthogonal(left: float, right: float, bottom: float, top: float, z_near: float, z_far: float) -> Projection:
	pass;

static func create_orthogonal_aspect(size: float, aspect: float, z_near: float, z_far: float, flip_fov: bool) -> Projection:
	pass;

static func create_perspective(fovy: float, aspect: float, z_near: float, z_far: float, flip_fov: bool) -> Projection:
	pass;

static func create_perspective_hmd(fovy: float, aspect: float, z_near: float, z_far: float, flip_fov: bool, eye: int, intraocular_dist: float,  convergence_dist: float) -> Projection:
	pass;

func determinant() -> float:
	pass;

func flipped_y() -> Projection:
	pass;

func get_aspect() -> float:
	pass;

func get_far_plane_half_extents() -> Vector2:
	pass;

func get_fov() -> float:
	pass;

static func get_fovy(fovx: float, aspect: float) -> float:
	pass;

func get_lod_multiplier() -> float:
	pass;

func get_pixels_per_meter(for_pixel_width: int) -> int:
	pass;

func get_projection_plane(plane: int) -> Plane:
	pass;

func get_viewport_half_extents() -> Vector2:
	pass;

func get_z_far() -> float:
	pass;

func get_z_near() -> float:
	pass;

func inverse() -> Projection:
	pass;

func is_orthogonal() -> bool:
	pass;

func jitter_offseted(offset: Vector2) -> Projection:
	pass;

func perspective_znear_adjusted(new_znear: float) -> Projection:
	pass;


