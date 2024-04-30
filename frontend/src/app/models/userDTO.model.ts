export interface UserDTO {
    name?: string | null;
    roles?: string[] | null;
    password?: string | null;
    listasDeElementos?: Map<string, number[]> | null;
    profileImageUrl?: string;
    bannerImageUrl?: string;
    profileImage?: File;
    bannerImage?: File;
}
